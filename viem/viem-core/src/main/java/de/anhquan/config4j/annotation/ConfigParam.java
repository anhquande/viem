/**
 * Copyright 2009 http://anhquan.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.anhquan.config4j.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ConfigParam {

        String Key() default "";
        String Title() default "";
        String Description() default "";
        int SortValue() default 0;
        
        int DefaultInteger() default 0;
        long DefaultLong() default 0;
        double DefaultDouble() default 0;
        char DefaultChar() default 0;
        short DefaultShort() default 0;
        byte DefaultByte() default 0;
        float DefaultFloat() default 0;
        boolean DefaultBoolean() default false;
        String DefaultString() default "[unassigned]";
        
        @SuppressWarnings("unchecked")
        Class DefaultClass() default Object.class;

}
