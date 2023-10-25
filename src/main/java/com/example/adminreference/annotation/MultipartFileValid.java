package com.example.adminreference.annotation;


import com.example.adminreference.config.MultipartFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.function.Function;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MultipartFileValid.List.class)
@Constraint(validatedBy = MultipartFileValidator.class)
public @interface MultipartFileValid {


    boolean nullable() default true;

    /**
     * Validation 걸린 경우 메세지 지정
     * 없는 경우 자동으로 메세지 나감.
     */
    String message() default "";

    /**
     * MultipartFile 사이즈 제한 크기
     * dataSizeType과 같이 사용됨
     */
    long limitSize() default 0L;

    /**
     * 파일 용량 타입 BYTE, KB, MB
     * limitSize 지정에 사용
     * ex)
     * limitSize = 10
     * dataSizeType = MB
     * 실제 데이터 제한 10 * 1024 * 1024 Byte
     */
    DataSizeType dataSizeType() default DataSizeType.MB;

    /**
     * 허용가능한 파일 확장자
     */
    String[] allowFileExtensions() default {};

    /**
     * 응답메세지 사용값
     * ex)
     * name = "대표이미지"
     * errorMessage = "대표이미지는 10MB 이하 입니다."
     */
    String name() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    /**
     * Defines several {@link MultipartFileValid} annotations on the same element.
     *
     * @see javax.validation.constraints.NotNull
     */
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {

        MultipartFileValid[] value();
    }

    enum DataSizeType {
        BYTE(num -> num), KB(num -> num * 1024), MB(num -> num * 1024 * 1024);

        private final Function<Long, Long> expression;

        DataSizeType(Function<Long, Long> expression) {
            this.expression = expression;
        }

        public long apply(long value) {
            return this.expression.apply(value);
        }
    }
}
