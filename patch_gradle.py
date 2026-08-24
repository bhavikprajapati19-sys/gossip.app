content = open('app/build.gradle.kts').read()

content = content.replace(
    'storePassword = System.getenv("STORE_PASSWORD")',
    'storePassword = System.getenv("STORE_PASSWORD") ?: "android"'
).replace(
    'keyPassword = System.getenv("KEY_PASSWORD")',
    'keyPassword = System.getenv("KEY_PASSWORD") ?: "android"'
)

open('app/build.gradle.kts', 'w').write(content)
