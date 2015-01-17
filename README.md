# uirepo
集成方法
1、运行代码，gradle clean uploadArchives 发布到本地maven仓库
2、填加依赖到项目，
repositories {
    mavenLocal()
}

dependencies {
    compile 'com.uirepo.mbui:library:1.0.2'
}
