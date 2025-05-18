API 使用方法（无需打包到依赖，用本项目作为前置插件即可）：
```Gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    CompileOnly 'com.github.MoYuSOwO:NeoArtisan:-SNAPSHOT:api'
    // 或稳定版 CompileOnly 'com.github.MoYuSOwO:NeoArtisan:Realse版本号:api' 
}
```
