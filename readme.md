## 简介

    restdocs通过手工编写文档模板，测试生成代码的形式来生成文档

## 依赖版本
   + springboot 2.1.9.RELEASE
   + spring-restdocs-mockmvc 2.0.4.RELEASE
   + spring-restdocs-asciidoctor 2.0.3.RELEASE(2.0.4下载不下来)
   
## 引入依赖
```
    <dependency> 
        <groupId>org.springframework.restdocs</groupId>
        <artifactId>spring-restdocs-mockmvc</artifactId>
        <version>2.0.3.RELEASE</version>
        <scope>test</scope>
    </dependency>
    
    <build>
        <plugins>
            <plugin> 
                <groupId>org.asciidoctor</groupId>
                <artifactId>asciidoctor-maven-plugin</artifactId>
                <version>1.5.3</version>
                <executions>
                    <execution>
                        <id>generate-docs</id>
                        <phase>prepare-package</phase> 
                        <goals>
                            <goal>process-asciidoc</goal>
                        </goals>
                        <configuration>
                            <backend>html</backend>
                            <doctype>book</doctype>
                        </configuration>
                    </execution>
                </executions>
                <dependencies>
                    <dependency> 
                        <groupId>org.springframework.restdocs</groupId>
                        <artifactId>spring-restdocs-asciidoctor</artifactId>
                        <version>2.0.3.RELEASE</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
```

## 构建测试环境

   + 示例使用maven构建项目，是使用junit进行测试，测试类上加上注解
        @SpringBootTest
        @RunWith(SpringRunner.class)
   + 添加依赖
     ```
        @Rule
        public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
     ```
     默认请下，代码片段会生成到target/generated-snippets目录下
   + 创建MockMvc
     ```
        @Autowired
        private WebApplicationContext applicationContext;
           
        @Before
        public void before(){
            this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                    .apply(springSecurity())
                    .apply(documentationConfiguration(this.restDocumentation)
                            .operationPreprocessors()
                            .withRequestDefaults(prettyPrint())
                            .withResponseDefaults(prettyPrint())
                            .and()
                            .uris()
                            .withScheme("http")
                            .withHost("localhost")
                            .withPort(8080).and()
                    ).build();
        }
     ```

## 构建测试代码

   ```
    @Test
    public void saveUser() throws Exception {
        User user = User.of()
                .setUserId(1l)
                .setUserName("tom")
                .setUserCaption("汤姆")
                .setAge(22)
                .setTelephone("15112311111")
                .setEmail("tom@123.com");
        this.mockMvc.perform(
                post("/user")
                .content(new ObjectMapper().writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("saveUser"
                ));
    }
   ```

## 生成片段
    
   在target/generated-snippets目录下可以看到 saveUser/xxx.adoc,其中adoc一共 有以下几个部分，针对不同类型请求与响应的片段

   + 代码片段包括以下几个部分\
        <output-directory>/index/curl-request.adoc
        
        <output-directory>/index/http-request.adoc
        
        <output-directory>/index/http-response.adoc
        
        <output-directory>/index/httpie-request.adoc
        
        <output-directory>/index/request-body.adoc
        
        <output-directory>/index/response-body.adoc


## 构建*.adoc

   + 编写asciidoc文档模板:
      + 模板语法
      
            include::{snippets}/index/curl-request.adoc[]
            operation::xxxClassName/xxxmethodName[snippets='curl-request,request-fields,response-body,response-fields']
    
      + 模板示例
      
            == 用户管理
            
            === 接口说明
                系统用户的增删改查
            
            === 接口调用
            
            ==== 保存用户
            
            include::{snippets}/saveUser/curl-request.adoc[]
            
            include::{snippets}/saveUser/http-request.adoc[]
            
            include::{snippets}/saveUser/http-response.adoc[]
            
            include::{snippets}/saveUser/httpie-request.adoc[]

## 生成html文档

    直接执行mvn package则可生成对应离线文档，文档位置 target/generated-docs

## 生成pdf文档

    添加asciidoctorj-pdf依赖，网上下载的很多对中文支持都不好，同时asciidoctor-maven-plugin版本过高(1.5.8)会有问题，更换到1.5.5\
    记得在插件中加上生成pdf的节点<execution>
    
## 参考

   [官网](https://docs.spring.io/spring-restdocs/docs/2.0.3.RELEASE/reference/html5/)