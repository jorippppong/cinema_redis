package com.cinema.core.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.type.StandardBasicTypes;

public class MySQL8DialectCustom extends MySQLDialect {

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);

        SqmFunctionRegistry functionRegistry = functionContributions.getFunctionRegistry();

        functionRegistry.registerPattern(
                "match_against",
                "match(?1) against (?2 in boolean mode)",
                functionContributions.getTypeConfiguration().getBasicTypeRegistry()
                        .resolve(StandardBasicTypes.DOUBLE)
        );
    }
}
