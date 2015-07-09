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
import com.consol.citrus.dsl.definition.GroovyActionDefinition;
import com.consol.citrus.dsl.runner.TestActionConfigurer;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import org.testng.annotations.Test;

/**
 * @author Christoph Deppisch
 */
@Test
public class CreateVariablesTestRunnerITest extends TestNGCitrusTestRunner {
    
    @CitrusTest
    public void CreateVariablesTestRunnerITest() {
        variable("myVariable", "12345");
        variable("newValue", "54321");
        
        echo("Current variable value: ${myVariable}");
        
        createVariable("myVariable", "${newValue}");
        createVariable("new", "This is a test");
        
        echo("Current variable value: ${myVariable}");
        
        echo("New variable 'new' has the value: ${new}");
        
        groovy(new TestActionConfigurer<GroovyActionDefinition>() {
            @Override
            public void configure(GroovyActionDefinition definition) {
                definition.script("assert ${myVariable} == 54321");
            }
        });

        createVariable("foo", "bar");

        echo("foo = '${foo}'");
    }
}