/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Multimap;
import com.parkhomenko.common.Utils.LoginData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiListingScanningContext;
import springfox.documentation.spring.web.scanners.ApiModelReader;

/**
 *
 * @author dmytro
 */
public class FormLoginOperations extends ApiListingScanner {

    @Autowired
    private TypeResolver typeResolver;

    @Autowired
    public FormLoginOperations(ApiDescriptionReader apiDescriptionReader, ApiModelReader apiModelReader, DocumentationPluginsManager pluginsManager) {
        super(apiDescriptionReader, apiModelReader, pluginsManager);
    }

    @Override
    public Multimap<String, ApiListing> scan(ApiListingScanningContext context) {
        final Multimap<String, ApiListing> def = super.scan(context);

        final List<ApiDescription> apis = new LinkedList<>();

        final List<Operation> operations = new ArrayList<>();
        operations.add(new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .uniqueId("login")
                .parameters(Arrays.asList(
                        new ParameterBuilder()
                                .name("password")
                                .description("The password")
                                .parameterType("body")
                                .type(typeResolver.resolve(LoginData.class))
                                .modelRef(new ModelRef("LoginData"))
                                .build()))
                .summary("Log in for admin") // 
                .notes("Here you can log in as admin")
                .build());
        apis.add(new ApiDescription("/admins/login", "Authentication admin", operations, false));

        def.put("authentication", new ApiListingBuilder(context.getDocumentationContext().getApiDescriptionOrdering())
                .apis(apis)
                .description("Authentication admin")
                .build());

        return def;
    }
}
