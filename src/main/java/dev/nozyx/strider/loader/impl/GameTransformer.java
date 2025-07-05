package dev.nozyx.strider.loader.impl;

import dev.nozyx.strider.loader.api.IGameTransformer;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;;

final class GameTransformer implements IGameTransformer {
    private final Instrumentation instrumentation;

    GameTransformer(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public void registerMethodInjection(String className, String methodName, String methodDescriptor, Class<?> adviceClass) {
        ElementMatcher<MethodDescription> matcher;
        if (methodName.equals("<init>")) matcher = ElementMatchers.isConstructor().and(ElementMatchers.hasDescriptor(methodDescriptor));
        else matcher = ElementMatchers.named(methodName).and(ElementMatchers.hasDescriptor(methodDescriptor));

        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(className))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.visit(Advice.to(adviceClass).on(matcher)))
                .installOn(instrumentation);
    }

    @Override
    public void registerMethodInjection(String className, String methodName, Class<?> adviceClass) {
        ElementMatcher<MethodDescription> matcher;
        if (methodName.equals("<init>")) matcher = ElementMatchers.isConstructor();
        else matcher = ElementMatchers.named(methodName);

        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(className))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.visit(Advice.to(adviceClass).on(matcher)))
                .installOn(instrumentation);
    }

    @Override
    public void registerMethodReplacement(String className, String methodName, String methodDescriptor, Class<?> methodDelegationClass) {
        ElementMatcher<MethodDescription> matcher;
        if (methodName.equals("<init>")) matcher = ElementMatchers.isConstructor().and(ElementMatchers.hasDescriptor(methodDescriptor));
        else matcher = ElementMatchers.named(methodName).and(ElementMatchers.hasDescriptor(methodDescriptor));

        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(className))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.method(matcher).intercept(MethodDelegation.to(methodDelegationClass)))
                .installOn(instrumentation);
    }

    @Override
    public void registerMethodReplacement(String className, String methodName, Class<?> methodDelegationClass) {
        ElementMatcher<MethodDescription> matcher;
        if (methodName.equals("<init>")) matcher = ElementMatchers.isConstructor();
        else matcher = ElementMatchers.named(methodName);

        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(className))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.method(matcher).intercept(MethodDelegation.to(methodDelegationClass)))
                .installOn(instrumentation);
    }

    @Override
    public void registerFieldReplacement(String className, String fieldName, String newValue) {
        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(className))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.field(ElementMatchers.named(fieldName)).value(newValue))
                .installOn(instrumentation);
    }

    @Override
    public void registerFieldReplacement(String className, String fieldName, boolean newValue) {
        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(className))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.field(ElementMatchers.named(fieldName)).value(newValue))
                .installOn(instrumentation);
    }

    @Override
    public void registerFieldReplacement(String className, String fieldName, int newValue) {
        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(className))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.field(ElementMatchers.named(fieldName)).value(newValue))
                .installOn(instrumentation);
    }

    @Override
    public void registerFieldReplacement(String className, String fieldName, long newValue) {
        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(className))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.field(ElementMatchers.named(fieldName)).value(newValue))
                .installOn(instrumentation);
    }

    @Override
    public void registerFieldReplacement(String className, String fieldName, float newValue) {
        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(className))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.field(ElementMatchers.named(fieldName)).value(newValue))
                .installOn(instrumentation);
    }

    @Override
    public void registerFieldReplacement(String className, String fieldName, double newValue) {
        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(className))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.field(ElementMatchers.named(fieldName)).value(newValue))
                .installOn(instrumentation);
    }

    @Override
    public void registerMethodInjection(Class<?> clazz, String methodName, String methodDescriptor, Class<?> adviceClass) {
        registerMethodInjection(clazz.getName(), methodName, methodDescriptor, adviceClass);
    }

    @Override
    public void registerMethodInjection(Class<?> clazz, String methodName, Class<?> adviceClass) {
        registerMethodInjection(clazz.getName(), methodName, adviceClass);
    }

    @Override
    public void registerMethodReplacement(Class<?> clazz, String methodName, String methodDescriptor, Class<?> methodDelegationClass) {
        registerMethodReplacement(clazz.getName(), methodName, methodDescriptor, methodDelegationClass);
    }

    @Override
    public void registerMethodReplacement(Class<?> clazz, String methodName, Class<?> methodDelegationClass) {
        registerMethodReplacement(clazz.getName(), methodName, methodDelegationClass);
    }

    @Override
    public void registerFieldReplacement(Class<?> clazz, String fieldName, String newValue) {
        registerFieldReplacement(clazz.getName(), fieldName, newValue);
    }

    @Override
    public void registerFieldReplacement(Class<?> clazz, String fieldName, boolean newValue) {
        registerFieldReplacement(clazz.getName(), fieldName, newValue);
    }

    @Override
    public void registerFieldReplacement(Class<?> clazz, String fieldName, int newValue) {
        registerFieldReplacement(clazz.getName(), fieldName, newValue);
    }

    @Override
    public void registerFieldReplacement(Class<?> clazz, String fieldName, long newValue) {
        registerFieldReplacement(clazz.getName(), fieldName, newValue);
    }

    @Override
    public void registerFieldReplacement(Class<?> clazz, String fieldName, float newValue) {
        registerFieldReplacement(clazz.getName(), fieldName, newValue);
    }

    @Override
    public void registerFieldReplacement(Class<?> clazz, String fieldName, double newValue) {
        registerFieldReplacement(clazz.getName(), fieldName, newValue);
    }
}
