package com.example.demo.service;

import com.google.common.collect.Lists;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ted on 2018/9/20.
 */
@Service
@Lazy(false)
public class CommonService {

    public List<Integer> list;

    public CommonService() {
        this.list = new ArrayList<>();
    }

    public List<Integer> getMsg(Boolean flag) {
        if (flag) {
            list.add(1);
            list.add(2);
            list.add(3);
        } else {
            list.add(1);
            list.add(2);
            list.add(3);
            list.add(4);

        }
        return this.list;
    }

}
