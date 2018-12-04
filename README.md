# Bean-Bouncer

Staying on top of all bean scopes in Spring can be challenging, especially when you are working with custom scopes. Of course you can create scope proxies for everything or always inject your dependencies using a `Provider<T>` but this still isn't fail safe. Sooner or later, someone will forget about the `Provider<T>` or define a new scope without enabling proxy mode as it was maybe not needed for the initial use case.
So instead of relying on conventions, bean-bouncer prevents you from injecting beans with a narrower scope unless you explicitly allow it.

## How it works

Bean-bouncer uses a `BeanPostProcessor` to intercept the creation of beans. Whenever a new bean is created, `BeanPostProcessor`  validates the scope of all bean dependencies. By default, the following injections are allowed:

* Singletons can be injected into everything
* Everything can be injected into prototypes
* AOP proxies can be injected into everything
* Everything can be injected into objects of the same scope

Everything else needs to be allowed explicitly. If a bean with a not allowed scope should be injected, it can throw an exception, log an error or perform any custom action.

## Usage

If you're using maven, add the following dependency to your `pom.xml`:

```
<dependency>
  <groupId>de.centerdevice</groupId>
  <artifactId>bean-bouncer</artifactId>
  <version>0.0.1</version>
</dependency>
```

Load the bean-bouncer default configuration:

```
@Configuration
@Import(BeanBouncerDefaultConfiguration.class)
public class MyConfiguration {
 //...
}
```

And that is it. To allow a bean to be injected into another scope, use the `@InjectableInto` annotation:

```
@Bean
@Scope("prototype")
@InjectableInto("singleton")
MyBean getMyBean(){
 //...
}
```
