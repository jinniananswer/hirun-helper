从 3.1 开始，Spring 引入了对 Cache 的支持。其使用方法和原理都类似于 Spring 对事物的支持。Spring Cache 是作用在方法上的，
其核心思想是这样的：当我们在调用一个缓存方法时会把该方法参数和返回结果作为一个键值对存放在缓存中，等下次利用同样的参数来
调用该方法时将不再执行该方法，而是直接从缓存中获取结果进行返回。所以在使用 Spring Cache 的时候我们要保证缓存的方法对于相
同的方法参数要有相同的返回结果。

使用 Spring Cache 需要做两方面的事情：

	* 声明某些方法使用缓存。
	* 配置 Spring 对 Cache 的支持。

和 Spring 对事务管理的支持一样，Spring 对 Cache 的支持也有基于注解和基于 XML 配置两种方式。

### 基于注解的支持

Spring 为我们提供了几个注解来支持 Spring Cache。其核心主要是 @Cacheable 和 @CacheEvict。使用 @Cacheable 标记的方法在执行后 
Spring Cache 将缓存其返回结果，而使用 @CacheEvict 标记的方法会在方法执行前或者执行后移除 Spring Cache 中的某些元素。

#### @Cacheable

@Cacheable 可以标记在一个方法上，也可以标记在一个类上。当标记在一个方法上时表示该方法是支持缓存的，当标记在一个类上时则表示该
类所有的方法都是支持缓存的。对于一个支持缓存的方法，Spring 会在其被调用后将其返回值缓存起来，以保证下次利用同样的参数来执行该
方法时可以直接从缓存中获取结果，而不需要再次执行该方法。Spring 在缓存方法的返回值时是以键值对进行缓存的，值就是方法的返回结果，
至于键的话，Spring 又支持两种策略，默认策略和自定义策略。需要注意的是当一个支持缓存的方法在对象内部被调用时是不会触发缓存功能
的。其次是确保被缓存的方法是无状态的，比如不会往会话 Session 里存放数据等。

@Cacheable 可以指定三个属性：value，key，condition。

#### value 属性指定 cache 名称

value 属性是必须指定的，其表示当前方法的返回值会被缓存在哪个 Cache 上，对应 Cache 的名称。其可以是一个 Cache 也可以是多个 Cache，
当需要指定多个 Cache 时其是一个数组。

```java
@Cacheable("cache1") // Cache 发生在 cache1 上
public User find(Long id) {
    ...
}
```

```java
@Cacheable({"cache1", "cache2"}) // Cache 发生在 cache1 和 cache2 上
public User find(Long id) {
    ...
}
```

#### 使用 Key 属性自定义 key

Key 属性是用来指定 Spring 缓存方法的返回结果对应的 key，该属性支持 SpringEL 表达式。当我们没有指定该属性时，Spring 将使用默认策略生成 Key。

自定义策略是指我们可以通过 Spring 的 EL 表达式来指定我们的 Key，这里的 EL 表达式可以使用方法参数以及它们对应的属性。使用方法参数时我们可以直接使用 “#参数名” 或者 "#p参数index"。

```java
@Cacheable(value = "users", key = "#id")
public User find(Long id) {
    ...
}

@Cacheable(value = "users", key = "#p0")
public User find(Long id) {
    ...
}

@Cacheable(value = "users", key = "#user.id")
public User find(User user) {
    ...
}

@Cacheable(value = "users", key = "#p0.id")
public User find(User user) {
   ...
}
```

#### condition 属性指定发生的条件

​有的时候我们可能并不希望缓存一个方法所有的返回结果。通过 condition 属性可以实现这一功能。condition 属性默认为空，表示将缓存所有的调用情形。其值是通过 SpringEL 表达式来指定的，当为 true 时表示进行缓存处理；当为 false 时表示不进行缓存处理，即每次调用该方法时该方法都会执行一次。

```java
@Cacheable(value = {"users"}, key = "#user.id", condition = "#user.id%2==0")
public User find(User user) {
    System.out.println("find user by user " + user);
    return user;
}
```

#### @CachePut

在支持 Spring Cache 的环境下，对于使用 @Cacheable 标注的方法，Spring 每次执行前都会检查 Cache 中是否存在相同 key 的缓存元素，如果存在就不再执行该方法，而是直接从缓存中获取结果进行返回，否则才会执行并将返回结果存入指定的缓存中。
@CachePut 也可以声明一个方法支持缓存功能，与 @Cacheable 不同的是使用 @CachePut 标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果，而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中。

```java
@CachePut("users") // 每次都会执行方法，并将结果存入指定的缓存
public User find(Long id) {
    ...
}
```

#### @CacheEvict

@CacheEvict 是用来标注在需要清除缓存元素的方法或类上，当标记在一个类上时，表示其中所有的方法的执行都会触发缓存的清除操作。@CacheEvict 可以指定的属性有 value、key、condition、allEntries、beforeInvocation。其中 value、key、condition 的语义跟 @Cacheable 对应的属性类似。

#### allEntries 属性

allEntries 是 boolean 类型，表示是否需要清除缓存中所有的元素。当为 true 时，Spring Cache 将忽略指定的 key。有时候我们需要一次性清除所有的元素，这比一个一个清除更有效率。

```java
@CacheEvict(value = "users", allEntries = true)
public void delete(Long id) {
    ...
}
```

#### beforeInvocation 属性

清除操作默认是在对应方法成功执行之后触发的，如果方法因为抛出异常未能成功返回是不会触发清除操作的。使用 beforeInvocation = true 时，Spring 会在调用该方法之前清除缓存中指定的元素。

```java
@CacheEvict(value = "users", beforeInvocation = true)
public void delete(Long id) {
    ...
}
```

#### @Caching

@Caching 注解可以让我们在一个方法或者类上同时指定多个 Spring Cache 相关的注解。

```java
@Caching(
   cacheable = @Cacheable("users"),
   evict = {
      @CacheEvict("cache2"),
      @CacheEvict(value = "cache3", allEntries = true)
   }
)
public User find(Long id) {
   ...
}
```