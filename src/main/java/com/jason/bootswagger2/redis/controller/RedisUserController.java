package com.jason.bootswagger2.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/redisUser")
public class RedisUserController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /******************************************************************StringRedisTemplate**************************************************************************/
    /**
     * 设置并获取之间的结果，要求key，value都不能为空；如果之前没有值，返回null
     *
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("strSet")
    @ResponseBody
    public Object setAndGetOldValue(String key, Long value) {

        stringRedisTemplate.execute( (RedisCallback<Boolean>) (con) -> {
            boolean b = con.setBit(key.getBytes(), value, true);
            //失效时间
            con.setEx(key.getBytes(),50,con.get(key.getBytes()));
            return b;
        });

        boolean getBit = stringRedisTemplate.execute((RedisCallback<Boolean>) con -> con.getBit(key.getBytes(), value));

        long bitCount = stringRedisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(key.getBytes()));

        return "123";//stringRedisTemplate.execute((RedisCallback<byte[]>) con -> con.getSet(key.getBytes(), value.getBytes()));
    }


    /******************************************************************String**************************************************************************/
    /**
     * 登录后保存用户名为userName,value为password
     * @param userName
     * @param password
     * @return
     */
    @GetMapping("/login")
    @ResponseBody
    public String login(@RequestParam("userName")String userName, @RequestParam("password")String password){
        redisTemplate.opsForValue().set(userName, password);
        redisTemplate.expire(userName,30, TimeUnit.SECONDS);
        return "success";
    }

    /**
     * 根据key获取value
     * @param key
     * @return
     */
    @GetMapping("/get")
    @ResponseBody
    public String getRedisVal(@RequestParam String key){
        String value = redisTemplate.opsForValue().get(key).toString();
        return value;
    }

    /**
     * 根据key设置value
     * @param key
     * @param value
     * @return
     */
    @GetMapping("/set")
    @ResponseBody
    public String setRedisVal(@RequestParam String key, @RequestParam Integer value){
        redisTemplate.opsForValue().set(key , value);
        return "success";
    }

    /**
     * 原子自增，设置过期时间
     * @param params
     * @return
     */
    @RequestMapping(value = "/incrementScore", method = RequestMethod.POST)
    @ResponseBody
    public String incrementScore(@RequestBody Map<String, Object> params) {
        String key = params.get("key").toString();
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        entityIdCounter.set(increment+1);
        entityIdCounter.expire(300, TimeUnit.SECONDS);//过期时间为3秒
        return "success";
    }



    /*****************************************************************************Zset******************************************************************/

    /**
     * 实现排序，热度，积分榜等功能，更多方法可以可以搜索zSetOperations
     * 如果要获取所有 0，-1
     * zSetOperations.range("score",0,-1);
     * @return
     */
    @GetMapping(value = "rankScore")
    @ResponseBody
    public String rank(){
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("score","one",1);
        zSetOperations.add("score","four",4);
        zSetOperations.add("score","three",110);
        zSetOperations.add("score","five",5);
        zSetOperations.add("score","six",6);
        //从子集中找到Smin<=score<=Smax的元素集合
        //value在此区间中的。
        Set set = zSetOperations.rangeByScore("score", 100,120);
        System.out.println("打印v1的值在100-120区间的"+set.size());

        //索引start<=index<=end的元素子集，返回泛型接口（包括score和value），正序
        //返回score和value，set中的前两个
        Set set1 = zSetOperations.rangeWithScores("score",0,1);

        //键为K的集合，索引start<=index<=end的元素子集，正序
        //返回排序后的value中的前两个
        Set set2 = zSetOperations.range("score",0,1);

        //键为K的集合，索引start<=index<=end的元素子集，倒序
        //返回排序后的最后两个
        Set set3 = zSetOperations.reverseRange("score",0,1);

        Set set4 = zSetOperations.reverseRangeWithScores("score",0,1);
        return null;
    }
    /******************************************************************Set**************************************************************************/

    @ResponseBody
    @RequestMapping("/redisSet")
    public Object redisSet(){
        SetOperations<String,Object> setOperations = redisTemplate.opsForSet();
        setOperations.add("set:1","测试1");
        setOperations.add("set:1","测试2");

        //移除
        Long result = setOperations.remove("set:1","测试1");

        //判断是否包含
        boolean contions = setOperations.isMember("set:1","测试1");

        Set<Object> set = setOperations.members("set:1");

        setOperations.add("set:2","测试3");


        //返回多个集合的并集  sunion
        Set<Object> set1 =  setOperations.union("set:1", "set:2");

        //返回多个集合的交集 sinter
        Set<Object> set2 = setOperations.intersect("set:1", "set:2");

        //返回集合key1中存在，但是key2中不存在的数据集合  sdiff
        Set<Object> set3 = setOperations.difference("set:1", "set:2");

        return null;
    }



    /******************************************************************List**************************************************************************/

    /**
     *左边插入
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/leftPush")
    @ResponseBody
    public String leftPush(@RequestParam(value = "key",required = true) String key,@RequestParam(value = "value",required = true) String value){
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPush(key,value);
        redisTemplate.expire(key,300,TimeUnit.SECONDS);
        return "success";
    }

    /**
     * 右边插入
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/rightPush")
    @ResponseBody
    public String rightPush(@RequestParam(value = "key",required = true) String key,@RequestParam(value = "value",required = true) String value){
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.rightPush(key,value);
        redisTemplate.expire(key,300,TimeUnit.SECONDS);
        return "success";
    }

    /**
     * 获取指定索引位置的值, index为-1时，表示返回的是最后一个；当index大于实际的列表长度时，返回null
     * 与jdk中的List获取某个索引value不同的是，这里的index可以为负数，-1表示最右边的一个，-2则表示最右边的第二个，依次
     * @param index
     * @return
     */
    @RequestMapping("/getByIndex")
    @ResponseBody
    public Object getByIndex(String key,Integer index){
        ListOperations<String,Object> listOperations = redisTemplate.opsForList();
        return listOperations.index(key,index);
    }



    /**
     * 删除列表中值为value的元素，总共删除count次；
     *
     * 如原来列表为 【1， 2， 3， 4， 5， 2， 1， 2， 5】
     * 传入参数 value=2, count=1 表示删除一个列表中value为2的元素
     * 则执行后，列表为 【1， 3， 4， 5， 2， 1， 2， 5】
     *
     * @param key
     * @param value
     * @param count
     */
    public void remove(String key, String value, int count) {
        redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * 删除list首尾，只保留 [start, end] 之间的值
     *
     * @param key
     * @param start
     * @param end
     */
    public void trim(String key, Integer start, Integer end) {
        redisTemplate.opsForList().trim(key, start, end);
    }



    /**
     * 设置list中指定下标的值，采用干的是替换规则, 最左边的下标为0；-1表示最右边的一个
     *
     * @param key
     * @param index
     * @param value
     */
    public void set(String key, Integer index, String value) {
        redisTemplate.opsForList().set(key, index, value);
    }


    /**
     * 获取范围值，闭区间，start和end这两个下标的值都会返回; end为-1时，表示获取的是最后一个；
     *
     * 如果希望返回最后两个元素，可以传入  -2, -1
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> range(String key, int start, int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }


    //http://spring.hhui.top/spring-blog/2018/12/02/181202-SpringBoot%E9%AB%98%E7%BA%A7%E7%AF%87Redis%E4%B9%8BHash%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E4%BD%BF%E7%94%A8%E5%A7%BF%E5%8A%BF/
    /******************************************************************Hash**************************************************************************/

    @RequestMapping("/hash")
    @ResponseBody
    public Object hash(){
        String key = "hash:1";
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key,"1","1");
        hashOperations.put(key,"2","2");
        hashOperations.put(key,"3","3");

        //hash的value如果是数字，提供了一个自增的方式，和String中的incr/decr差不多的效果
        hashOperations.increment(key,"3",2);

        redisTemplate.execute((RedisCallback<Map<String,Object>>)con->{
            Map<byte[], byte[]> result = con.hGetAll(key.getBytes());
            if (CollectionUtils.isEmpty(result)) {
                return new HashMap<>(0);
            }

            Map<String, Object> ans = new HashMap<>(result.size());
            for (Map.Entry<byte[], byte[]> entry : result.entrySet()) {
                ans.put(new String(entry.getKey()), new String(entry.getValue()));
            }
            return ans;
        });


        List<String> fields = new ArrayList<>();
        fields.add("1");
        fields.add("2");
        fields.add("3");
        List<String> result = hashOperations.multiGet(key,fields);
        Map<String, String> ans = new HashMap<>(fields.size());
        int index = 0;
        for (String field : fields) {
            if (result.get(index) == null) {
                continue;
            }
            ans.put(field, result.get(index));
            index++;
        }
        return ans;
    }
}
