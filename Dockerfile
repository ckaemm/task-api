# Java 21 calistirmak icin JRE yeterli; derlemeyi host'ta Maven yapiyor.
FROM eclipse-temurin:21-jre

# Container icindeki calisma dizini. Sonraki komutlar buraya gore calisir.
WORKDIR /app

# Maven'in urettigi fat JAR'i sabit bir isimle kopyala.
# Surum numarasi degistiginde ENTRYPOINT'i duzeltmek gerekmesin diye app.jar.
COPY target/*.jar app.jar

# Uygulamanin dinledigi port. Bu satir belgeleme amaclidir; portu yayinlamaz.
EXPOSE 8080

# Exec formu: java PID 1 olur ve docker stop'un gonderdigi SIGTERM'i dogrudan alir.
ENTRYPOINT ["java", "-jar", "app.jar"]
