package com.lwang.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RedisTest.class
 *
 * @author lwang
 * @date 17-11-23.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * String 普通的存储
     */

    // key重复value会覆盖
    @Test
    public void valueAddResitTest() {
        stringRedisTemplate.opsForValue().set("lwang", "滴滴打车");
    }

    @Test
    public void valueGetResitTest() {
        String value = stringRedisTemplate.opsForValue().get("lwang");
        System.out.print("value:::" + value);
    }

    @Test
    public void valueDelResitTest() {
        stringRedisTemplate.delete("lwang");
    }

    // 存储的时间戳
    @Test
    public void valueTimeoutResitTest() {
        stringRedisTemplate.opsForValue().set("timeStep", new Date().getTime() + "", 2, TimeUnit.MINUTES);
    }



    /**
     * List 数据类型适合于消息队列的场景:比如12306并发量太高，而同一时间段内只能处理指定数量的数据！必须满足先进先出的原则，其余数据处于等待
     */

    // key重复value不会覆盖
    @Test
    public void listPushResitTest() {

        // leftPush依次由右边添加
        stringRedisTemplate.opsForList().rightPush("myList", "1");
        stringRedisTemplate.opsForList().rightPush("myList", "2");
        stringRedisTemplate.opsForList().rightPush("myList", "A");
        stringRedisTemplate.opsForList().rightPush("myList", "B");

        // leftPush依次由左边添加
        stringRedisTemplate.opsForList().leftPush("myList", "0");
    }

    @Test
    public void listGetListResitTest() {

        // 查询类别所有元素（参数：key名称  0从哪里开始   -1全部）
        List<String> listAll = stringRedisTemplate.opsForList().range("myList", 0, -1);
        System.out.print("listAll:::" + listAll);


        // 查询前3个元素（参数：key名称  0从哪里开始   3差多索引为3的地方）
        List<String> list = stringRedisTemplate.opsForList().range("myList", 0, 3);
        System.out.print("list:::" + list);
    }

    @Test
    public void listRemoveOneResitTest() {

        // 删除先进入的B元素（参数：key名称  1删除的数量   B要删除的内容）
        stringRedisTemplate.opsForList().remove("myList", 1, "B");
    }

    @Test
    public void listRemoveAllResitTest() {

        // 删除所有A元素（参数：key名称  0全部删除的   A要删除的内容）
        stringRedisTemplate.opsForList().remove("myList", 0, "A");
    }


    /**
     * Hash
     */

    @Test
    public void hashPutResitTest(){

        // map的key值相同，后添加的覆盖原有的（参数：banks:12600000key的名称  a要存的key  b要存的value）（key：value（key：value））
        stringRedisTemplate.opsForHash().put("banks:12600000", "a", "b");
    }

    @Test
    public void hashGetEntiresResitTest(){

        // 获取map对象
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries("banks:12600000");
        System.out.print("map:::" + map);
    }

    @Test
    public void hashGeDeleteResitTest(){

        // 根据map的key删除这个元素
        stringRedisTemplate.opsForHash().delete("banks:12600000", "c");
    }

    @Test
    public void hashGetKeysResitTest(){

        // 获得map的key集合
        Set<Object> objects =  stringRedisTemplate.opsForHash().keys("banks:12600000");
        System.out.print("objects:::" + objects);
    }

    @Test
    public void hashGetValueListResitTest(){

        // 获得map的value列表
        List<Object> objects = stringRedisTemplate.opsForHash().values("banks:12600000");
        System.out.print("objects:::" + objects);
    }

    @Test
    public void hashSize() {

        // 获取map对象大小
        long size =  stringRedisTemplate.opsForHash().size("banks:12600000");
        System.out.print("size:::" + size);
    }


}
