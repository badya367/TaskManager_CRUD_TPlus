package org.tplus.taskManager.taskManager_crud.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Аспект для логирования работы методов сервисного слоя.
 *
 * <p>Этот класс использует AOP (Aspect-Oriented Programming) для перехвата выполнения методов
 * и добавления логирования на различные этапы выполнения.</p>
 *
 * <p>Основные виды Advice, реализованные в этом аспекте:</p>
 * <ul>
 *     <li>{@code @Before} – логирование перед вызовом метода.</li>
 *     <li>{@code @AfterThrowing} – логирование в случае выбрасывания исключения.</li>
 *     <li>{@code @AfterReturning} – логирование после успешного выполнения метода.</li>
 *     <li>{@code @Around} – измерение времени выполнения метода с аннотацией {@link LogExecutionTime}.</li>
 * </ul>
 *
 * <p>Аннотации:</p>
 * <ul>
 *     <li>{@code @Aspect} – указывает, что класс является аспектом.</li>
 *     <li>{@code @Component} – делает аспект управляемым компонентом Spring.</li>
 *     <li>{@code @Slf4j} – добавляет поддержку логирования через SLF4J.</li>
 * </ul>
 *
 * @author Бадиков Дмитрий
 * @version 1.0
 * @since 2025-02-22
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {
    /**
     * Логирует вызов метода перед его выполнением.
     *
     * <p>Этот аспект применяется ко всем методам в пакете {@code services}.
     * Логирование происходит перед выполнением целевого метода.</p>
     */
    @Before("execution(* org.tplus.taskManager.taskManager_crud.services.TaskService.*(..))")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        log.info("Before: Метод вызывается - {}", joinPoint.getSignature().toShortString());
    }

    /**
     * Логирует исключение, выброшенное методом.
     *
     * <p>Этот аспект срабатывает, если в методе сервисного слоя возникает исключение.</p>
     *
     * @param ex выброшенное исключение
     */
    @AfterThrowing(pointcut = "execution(* org.tplus.taskManager.taskManager_crud.services.TaskService.*(..))", throwing = "ex")
    public void logAfterThrowing(Exception ex) {
        log.error("AfterThrowing: Метод выбросил исключение - {}", ex.getMessage());
    }

    /**
     * Логирует успешное выполнение метода и его возвращаемый результат.
     *
     * <p>Этот аспект срабатывает после успешного выполнения метода в пакете {@code services}.</p>
     *
     * @param result результат выполнения метода
     */
    @AfterReturning(pointcut = "execution(* org.tplus.taskManager.taskManager_crud.services.TaskService.*(..))", returning = "result")
    public void logAfterReturning(Object result) {
        log.info("AfterReturning: Метод успешно завершился. Результат: {}", result);
    }

    /**
     * Измеряет время выполнения метода, аннотированного {@link LogExecutionTime}.
     *
     * <p>Логирует время начала выполнения метода, затем выполняет его и логирует
     * время завершения с расчетом затраченного времени.</p>
     *
     * @param joinPoint информация о вызываемом методе
     * @return результат выполнения метода
     * @throws Throwable если метод выбрасывает исключение
     */
    @Around("@annotation(org.tplus.taskManager.taskManager_crud.aspects.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        log.info("Вызов метода: {}", methodName);

        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;

            log.info("Метод {} выполнен за {} мс", joinPoint.getSignature().toShortString(), executionTime);

            return result;
        } catch (Throwable ex) {
            log.warn("Метод вызвал исключение, время работы метода: {} ms", System.currentTimeMillis() - start);
            throw ex;
        }

    }
}