package com.mikaelfrancoeur.aoptalk.audit;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Aspect
@RequiredArgsConstructor
class AuditAspect {

    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private final AuditService auditService;

    @AfterReturning("@annotation(auditedAnnotation)") // AspectJ language
    public void beforeAudited(JoinPoint joinPoint, Audited auditedAnnotation) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Expression expression = parser.parseExpression(auditedAnnotation.expression()); // cache this for better performance
        EvaluationContext context = new MethodBasedEvaluationContext(
                new Object(),
                signature.getMethod(),
                joinPoint.getArgs(),
                parameterNameDiscoverer);

        Collection<String> auditableIds = asStringCollection(expression.getValue(context));
        auditableIds.forEach(id -> auditService.audit(auditedAnnotation.action(), id));
    }

    private Collection<String> asStringCollection(Object result) {
        Objects.requireNonNull(result, "expression of @Audit evaluated to null");

        return switch (result) {
            case String string -> List.of(string);
            case Collection<?> collection -> {
                collection.forEach(element -> Assert.isInstanceOf(String.class, element,
                        () -> "@Audit expression evaluated to collection with non-string element"));
                //noinspection unchecked
                yield (Collection<String>) collection;
            }
            default -> throw new RuntimeException(
                    "@Audit expression evaluated to non-string type %s"
                            .formatted(result.getClass().getName()));
        };
    }
}
