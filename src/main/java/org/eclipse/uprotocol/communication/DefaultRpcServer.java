/**
 * SPDX-FileCopyrightText: 2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.eclipse.uprotocol.communication;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.uprotocol.transport.UListener;
import org.eclipse.uprotocol.transport.UTransport;
import org.eclipse.uprotocol.transport.builder.UMessageBuilder;
import org.eclipse.uprotocol.uri.factory.UriFactory;
import org.eclipse.uprotocol.v1.UAttributes;
import org.eclipse.uprotocol.v1.UCode;
import org.eclipse.uprotocol.v1.UMessage;
import org.eclipse.uprotocol.v1.UMessageType;
import org.eclipse.uprotocol.v1.UStatus;
import org.eclipse.uprotocol.v1.UUri;

public class DefaultRpcServer implements RpcServer {
    // The transport to use for sending the RPC requests
    private final UTransport transport;

    // Map to store the request handlers so we can handle the right request on the server side
    private final ConcurrentHashMap<UUri, RequestHandler> mRequestsHandlers = new ConcurrentHashMap<>();

    // Generic listener to handle all RPC request messages
    private final UListener mRequestHandler = this::handleRequests;


    /**
     * Constructor for the DefaultRpcServer.
     * 
     * @param transport the transport to use for sending the RPC requests
     */
    public DefaultRpcServer (UTransport transport) {
        Objects.requireNonNull(transport, UTransport.TRANSPORT_NULL_ERROR);
        this.transport = transport;
    }


    /**
     * Register a handler that will be invoked when when requests come in from clients for the given method.
     *
     * <p>Note: Only one handler is allowed to be registered per method URI.
     *
     * @param method Uri for the method to register the listener for.
     * @param handler The handler that will process the request for the client.
     * @return Returns the status of registering the RpcListener.
     */
    @Override
    public UStatus registerRequestHandler(UUri method, RequestHandler handler) {
        Objects.requireNonNull(method, "Method URI missing");
        Objects.requireNonNull(handler, "Request listener missing");
        
        // Ensure the method URI matches the transport source URI 
        if (!method.getAuthorityName().equals(transport.getSource().getAuthorityName()) ||
            method.getUeId() != transport.getSource().getUeId() ||
            method.getUeVersionMajor() != transport.getSource().getUeVersionMajor()) {
            throw new UStatusException(UCode.INVALID_ARGUMENT, 
                "Method URI does not match the transport source URI");
        }
        try {
            mRequestsHandlers.compute(method, (key, currentHandler) -> {
                if (currentHandler != null) {
                    throw new UStatusException(UCode.ALREADY_EXISTS, "Handler already registered");
                }
                
                UStatus result = transport.registerListener(UriFactory.ANY, method, mRequestHandler);
                if (!result.getCode().equals(UCode.OK)) {
                    throw new UStatusException(result);
                }
                return handler;
            });
            return UStatus.newBuilder().setCode(UCode.OK).build();
        } catch (Exception e) {
            if (e instanceof UStatusException) {
                return ((UStatusException) e).getStatus();
            }
            return UStatus.newBuilder().setCode(UCode.INTERNAL).setMessage(e.getMessage()).build();
        }
    }


    /**
     * Unregister a handler that will be invoked when when requests come in from clients for the given method.
     * 
     * @param method Resolved UUri for where the listener was registered to receive messages from.
     * @param handler The handler for processing requests
     * @return Returns status of registering the RpcListener.
     */
    @Override
    public UStatus unregisterRequestHandler(UUri method, RequestHandler handler) {
        Objects.requireNonNull(method, "Method URI missing");
        Objects.requireNonNull(handler, "Request listener missing");
    
        // Ensure the method URI matches the transport source URI 
        if (!method.getAuthorityName().equals(transport.getSource().getAuthorityName()) ||
            method.getUeId() != transport.getSource().getUeId() ||
            method.getUeVersionMajor() != transport.getSource().getUeVersionMajor()) {
            throw new UStatusException(UCode.INVALID_ARGUMENT, 
                "Method URI does not match the transport source URI");
        }

        if (mRequestsHandlers.remove(method, handler)) {
            return transport.unregisterListener(UriFactory.ANY, method, mRequestHandler);
        } 
        return UStatus.newBuilder().setCode(UCode.NOT_FOUND).build();
    }


    /**
     * Generic incoming handler to process RPC requests from clients
     * @param request The request message from clients
     */
    private void handleRequests(UMessage request) {
        // Only handle request messages, ignore all other messages like notifications
        if (request.getAttributes().getType() != UMessageType.UMESSAGE_TYPE_REQUEST) {
            return;
        }
    
        final UAttributes requestAttributes = request.getAttributes();
        
        // Check if the request is for one that we have registered a handler for, if not ignore it
        final RequestHandler handler = mRequestsHandlers.remove(requestAttributes.getSink());
        if (handler == null) {
            return;
        }

        UPayload responsePayload;
        UMessageBuilder responseBuilder = UMessageBuilder.response(request.getAttributes());

        try {
            responsePayload = handler.handleRequest(request);
        } catch (Exception e) {
            UCode code = UCode.INTERNAL;
            responsePayload = null;
            if (e instanceof UStatusException statusException) {
                code = statusException.getStatus().getCode();
            } else {
                code = UCode.INTERNAL;
            }
            responseBuilder.withCommStatus(code);
        }
        
        // TODO: Handle error sending the response
        transport.send(responseBuilder.build(responsePayload));
    }
}
