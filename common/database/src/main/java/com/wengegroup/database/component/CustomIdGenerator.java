package com.wengegroup.database.component;


import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @Classname MyIdGenerator
 * @description 自定义id生成策略
 */
@Component
@AllArgsConstructor
public class CustomIdGenerator implements IdentifierGenerator {

    //private Sequence sequence;

    @Override
    public Number nextId(Object entity) {
        //return sequence.nextValue();
        return new Random().nextDouble();
    }
}
