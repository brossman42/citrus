/*
 * Copyright 2006-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.javadsl.runner;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.builder.*;
import com.consol.citrus.dsl.builder.BuilderSupport;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.exceptions.ValidationException;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 */
@Test
public class ValidateNamespacesTestRunnerITest extends TestNGCitrusTestRunner {
    
    @CitrusTest
    public void validateNamespaces() {
        echo("Test: Success with single namespace validation");
        
        send(new BuilderSupport<SendMessageBuilder>() {
            @Override
            public void configure(SendMessageBuilder builder) {
                builder.endpoint("testMessageSender")
                        .payload("<trq:TestRequest xmlns:trq=\"http://www.consol.de/schemas/test\">" +
                                "<Message>Hello</Message>" +
                                "</trq:TestRequest>");
            }
        });
        
        receive(new BuilderSupport<ReceiveMessageBuilder>() {
            @Override
            public void configure(ReceiveMessageBuilder builder) {
                builder.endpoint("testMessageReceiver")
                        .payload("<trq:TestRequest xmlns:trq=\"http://www.consol.de/schemas/test\">" +
                                "<Message>Hello</Message>" +
                                "</trq:TestRequest>")
                        .schemaValidation(false)
                        .validateNamespace("trq", "http://www.consol.de/schemas/test")
                        .timeout(5000);
            }
        });
        
        echo("Test: Success with multiple namespace validations");
        
        send(new BuilderSupport<SendMessageBuilder>() {
            @Override
            public void configure(SendMessageBuilder builder) {
                builder.endpoint("testMessageSender")
                        .payload("<trq:TestRequest xmlns:trq=\"http://www.consol.de/schemas/test\" xmlns:msg=\"http://www.consol.de/schemas/message\">" +
                                "<msg:Message>Hello</msg:Message>" +
                                "</trq:TestRequest>");
            }
        });
        
        receive(new BuilderSupport<ReceiveMessageBuilder>() {
            @Override
            public void configure(ReceiveMessageBuilder builder) {
                builder.endpoint("testMessageReceiver")
                        .payload("<trq:TestRequest xmlns:trq=\"http://www.consol.de/schemas/test\" xmlns:msg=\"http://www.consol.de/schemas/message\">" +
                                "<msg:Message>Hello</msg:Message>" +
                                "</trq:TestRequest>")
                        .schemaValidation(false)
                        .validateNamespace("trq", "http://www.consol.de/schemas/test")
                        .validateNamespace("msg", "http://www.consol.de/schemas/message")
                        .timeout(5000);
            }
        });
        
        echo("Test: Success with multiple nested namespace validations");
        
        send(new BuilderSupport<SendMessageBuilder>() {
            @Override
            public void configure(SendMessageBuilder builder) {
                builder.endpoint("testMessageSender")
                        .payload("<trq:TestRequest xmlns:trq=\"http://www.consol.de/schemas/test\">" +
                                "<msg:Message xmlns:msg=\"http://www.consol.de/schemas/message\">Hello</msg:Message>" +
                                "</trq:TestRequest>");
            }
        });
        
        receive(new BuilderSupport<ReceiveMessageBuilder>() {
            @Override
            public void configure(ReceiveMessageBuilder builder) {
                builder.endpoint("testMessageReceiver")
                        .payload("<trq:TestRequest xmlns:trq=\"http://www.consol.de/schemas/test\">" +
                                "<msg:Message xmlns:msg=\"http://www.consol.de/schemas/message\">Hello</msg:Message>" +
                                "</trq:TestRequest>")
                        .schemaValidation(false)
                        .validateNamespace("trq", "http://www.consol.de/schemas/test")
                        .validateNamespace("msg", "http://www.consol.de/schemas/message")
                        .timeout(5000);
            }
        });
        
        echo("Test: Failure because of missing namespace");
        
        send(new BuilderSupport<SendMessageBuilder>() {
            @Override
            public void configure(SendMessageBuilder builder) {
                builder.endpoint("testMessageSender")
                        .payload("<trq:TestRequest xmlns:trq=\"http://www.consol.de/schemas/test\">" +
                                "<Message>Hello</Message>" +
                                "</trq:TestRequest>");
            }
        });
        
        assertException(new BuilderSupport<AssertExceptionBuilder>() {
            @Override
            public void configure(AssertExceptionBuilder builder) {
                builder.exception(ValidationException.class);
            }
        }).when(
                receive(new BuilderSupport<ReceiveMessageBuilder>() {
                    @Override
                    public void configure(ReceiveMessageBuilder builder) {
                        builder.endpoint("testMessageReceiver")
                                .payload("<trq:TestRequest xmlns:trq=\"http://www.consol.de/schemas/test\">" +
                                        "<Message>Hello</Message>" +
                                        "</trq:TestRequest>")
                                .schemaValidation(false)
                                .validateNamespace("trq", "http://www.consol.de/schemas/test")
                                .validateNamespace("missing", "http://www.consol.de/schemas/missing")
                                .timeout(5000);
                    }
                })
        );
        
        echo("Test: Failure because of wrong namespace prefix");
        
        send(new BuilderSupport<SendMessageBuilder>() {
            @Override
            public void configure(SendMessageBuilder builder) {
                builder.endpoint("testMessageSender")
                        .payload("<wrong:TestRequest xmlns:wrong=\"http://www.consol.de/schemas/test\">" +
                                "<Message>Hello</Message>" +
                                "</wrong:TestRequest>");
            }
        });
        
        assertException(new BuilderSupport<AssertExceptionBuilder>() {
            @Override
            public void configure(AssertExceptionBuilder builder) {
                builder.exception(ValidationException.class);
            }
        }).when(
                receive(new BuilderSupport<ReceiveMessageBuilder>() {
                    @Override
                    public void configure(ReceiveMessageBuilder builder) {
                        builder.endpoint("testMessageReceiver")
                                .payload("<trq:TestRequest xmlns:trq=\"http://www.consol.de/schemas/test\">" +
                                        "<Message>Hello</Message>" +
                                        "</trq:TestRequest>")
                                .schemaValidation(false)
                                .validateNamespace("trq", "http://www.consol.de/schemas/test")
                                .timeout(5000);
                    }
                })
        );
        
        echo("Test: Failure because of wrong namespace uri");
        
        send(new BuilderSupport<SendMessageBuilder>() {
            @Override
            public void configure(SendMessageBuilder builder) {
                builder.endpoint("testMessageSender")
                        .payload("<trq:TestRequest xmlns:trq=\"http://www.consol.de/schemas/wrong\">" +
                                "<Message>Hello</Message>" +
                                "</trq:TestRequest>");
            }
        });
        
        assertException(new BuilderSupport<AssertExceptionBuilder>() {
            @Override
            public void configure(AssertExceptionBuilder builder) {
                builder.exception(ValidationException.class);
            }
        }).when(
                receive(new BuilderSupport<ReceiveMessageBuilder>() {
                    @Override
                    public void configure(ReceiveMessageBuilder builder) {
                        builder.endpoint("testMessageReceiver")
                                .payload("<trq:TestRequest xmlns:trq=\"http://www.consol.de/schemas/test\">" +
                                        "<Message>Hello</Message>" +
                                        "</trq:TestRequest>")
                                .schemaValidation(false)
                                .validateNamespace("trq", "http://www.consol.de/schemas/test")
                                .timeout(5000);
                    }
                })
        );
    }
}