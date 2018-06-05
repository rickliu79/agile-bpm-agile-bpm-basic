package com.dstz.base.api.executor;

/**
 * 执行器
 *
 * @param <T> 运行执行器需要的参数
 * @author aschs
 */
public interface Executor<T> extends Comparable<Executor<T>> {
    /**
     * 执行器的类型的枚举
     *
     * @author aschs
     */
    public enum TYPE {
        /**
         * 必要性执行器，没有这个执行器，执行器服务功能无法正常运行
         */
        NECESSARY("necessary", "必要性执行器"),
        /**
         * 非必要性执行器，没有这个执行器服务，服务能跑，但可能有些东西不完整
         */
        UNNECESSARY("unnecessary", "非必要性执行器");

        private String key;
        private String desc;

        private TYPE(String key, String desc) {
            this.key = key;
            this.desc = desc;
        }

        public String getKey() {
            return key;
        }

        public String getDesc() {
            return desc;
        }

        /**
         * <pre>
         * 根据key来判断是否跟当前一致
         * </pre>
         *
         * @param key
         * @return
         */
        public boolean equalsWithKey(String key) {
            return this.key.equals(key);
        }
    }

    /**
     * <pre>
     * 执行器的key
     * </pre>
     *
     * @return
     */
    String getKey();

    /**
     * <pre>
     * 执行器的名称
     * </pre>
     *
     * @return
     */
    String getName();

    /**
     * <pre>
     * 返回这个执行器的类型key
     * </pre>
     *
     * @return
     */
    String type();

    /**
     * <pre>
     * 返回校验器的别名
     * 多个以,分隔，eg：a,b,c,...
     * </pre>
     *
     * @return
     */
    String getCheckerKey();

    /**
     * <pre>
     * 序号
     * 用于某些执行器有先后顺序的
     * </pre>
     *
     * @return
     */
    int getSn();

    /**
     * <pre>
     * 运行这个执行器
     * 运行前要通过校验
     * </pre>
     *
     * @param param 运行的参数
     */
    void execute(T param);

}
