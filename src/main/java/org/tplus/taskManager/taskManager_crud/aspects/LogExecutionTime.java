package org.tplus.taskManager.taskManager_crud.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для измерения времени выполнения метода.
 *
 * <p>Эта аннотация применяется на уровне метода и используется в сочетании с AOP (Aspect-Oriented Programming)
 * для логирования времени выполнения аннотированных методов.</p>
 *
 * <p>Аннотация имеет:</p>
 * <ul>
 *     <li>{@code @Target(ElementType.METHOD)} – применяется только к методам.</li>
 *     <li>{@code @Retention(RetentionPolicy.RUNTIME)} – сохраняется во время выполнения,
 *         что позволяет использовать ее в AOP-аспекте.</li>
 * </ul>
 *
 * <p>Пример использования:</p>
 * <pre>
 * {@code
 * @LogExecutionTime
 * public void someMethod() {
 *     // код метода
 * }
 * }
 * </pre>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-02-22
 */
@Target(ElementType.METHOD)
// Аннотация доступна во время выполнения
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
}
