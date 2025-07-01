package dev.nozyx.strider.loader;

import dev.nozyx.strider.loader.api.IGameTransformer;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;;

final class GameTransformer implements IGameTransformer {
    private final Instrumentation instrumentation;

    GameTransformer(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    @Override
    public void registerMethodInjection(String className, String methodName, Class<?> adviceClass) {
        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(className))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.visit(Advice.to(adviceClass).on(ElementMatchers.named(methodName))))
                .installOn(instrumentation);
    }

    @Override
    public void registerMethodReplacement(String className, String methodName, Class<?> methodDelegationClass) {
        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.named(className))
                .transform((builder, typeDescription, classLoader, javaModule, protectionDomain) -> builder.method(ElementMatchers.named(methodName)).intercept(MethodDelegation.to(methodDelegationClass)))
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
}
