# Laboratorio 13: Service Discovery y API Gateway

Este laboratorio implementa un registro Eureka y un API Gateway como entrada única a seis aplicaciones Spring Boot. Las rutas de categorías y productos reproducen el procedimiento de la guía; las rutas de clientes y pedidos resuelven la actividad. Los servicios de negocio devuelven datos de ejemplo en memoria para que el flujo pueda ejecutarse sin bases de datos.

## Requisitos

- IntelliJ IDEA con soporte Maven y Java 17 o superior (en Project SDK y Maven Runner).
- Maven 3.9 o superior.
- Puertos locales libres: 8080, 8081, 8082, 8083, 8084 y 8761.
- Conexión inicial a Maven Central para descargar dependencias.

La guía solicita Spring Boot 3.5.15 y Java 17. Su POM de ejemplo usa 3.5.3, así que este repositorio conserva el valor explícito solicitado en el paso 1: Boot 3.5.15. Spring Cloud 2025.0.0 es la línea compatible con Boot 3.5.x.

## Estructura

| Módulo | Puerto | Responsabilidad |
| --- | ---: | --- |
| `eureka-server` | 8761 | Registro y descubrimiento de instancias |
| `gateway-service` | 8080 | Enrutamiento central mediante Eureka y LoadBalancer |
| `categoria-service` | 8081 | Ejemplo requerido por la guía |
| `producto-service` | 8082 | Ejemplo requerido por la guía |
| `cliente-service` | 8083 | Servicio publicado para la actividad |
| `pedido-service` | 8084 | Servicio publicado para la actividad |

## Abrir en IntelliJ IDEA

1. Descomprime o clona el repositorio y abre la carpeta raíz `Lab13_Desarrollo_Aplicaciones_Web` con **File / Open**.
2. IntelliJ detectará el `pom.xml` raíz. Selecciona **Open as Project** o **Load Maven Project**. Si no aparece, abre la ventana Maven y pulsa **Reload All Maven Projects**.
3. En **File / Project Structure**, configura un JDK 17 o superior y establece el nivel de lenguaje en 17. En **Settings / Build Tools / Maven / Runner**, usa el mismo JDK.
4. Espera a que Maven resuelva las dependencias. El POM raíz agrega todos los módulos bajo `microservicios/`.
5. Para cada módulo, ejecuta su clase `*Application` desde el botón verde de IntelliJ o desde la terminal con los comandos indicados abajo. Arranca primero Eureka, luego los cuatro servicios y finalmente el Gateway.

## Ejecución paso a paso

Abre una terminal en la raíz y compila el reactor:

```bash
mvn clean package
```

En seis terminales separadas (o seis configuraciones de ejecución de IntelliJ), ejecuta en este orden:

```bash
mvn -pl microservicios/eureka-server spring-boot:run
mvn -pl microservicios/categoria-service spring-boot:run
mvn -pl microservicios/producto-service spring-boot:run
mvn -pl microservicios/cliente-service spring-boot:run
mvn -pl microservicios/pedido-service spring-boot:run
mvn -pl microservicios/gateway-service spring-boot:run
```

1. Abre [http://localhost:8761](http://localhost:8761) y confirma que Eureka muestre `GATEWAY-SERVICE`, `CATEGORIA-SERVICE`, `PRODUCTO-SERVICE`, `CLIENTE-SERVICE` y `PEDIDO-SERVICE`.
2. Envía estas solicitudes al puerto 8080; no es necesario llamar directamente a los puertos 8081–8084:

| Método y URL | Resultado esperado |
| --- | --- |
| `GET http://localhost:8080/api/categorias` | Lista de categorías |
| `GET http://localhost:8080/api/productos` | Lista de productos |
| `GET http://localhost:8080/api/clientes` | Lista de clientes |
| `GET http://localhost:8080/api/pedidos` | Lista de pedidos |
| `GET http://localhost:8080/actuator/health` | Estado de salud del Gateway |

También puedes usar `curl.exe http://localhost:8080/api/clientes` y `curl.exe http://localhost:8080/api/pedidos` desde PowerShell. Las rutas configuradas en `microservicios/gateway-service/src/main/resources/application.yml` usan `lb://` para que Spring Cloud LoadBalancer elija una instancia anunciada en Eureka. El locator habilitado permite además rutas automáticas por ID de servicio.

## Qué hace el Gateway

El cliente envía la petición al puerto 8080. Spring Cloud Gateway compara la ruta con los predicados `Path`, consulta el registro Eureka para localizar el servicio y delega la petición mediante LoadBalancer. Los endpoints de negocio conservan su prefijo `/api/...`, por lo que cada ruta del Gateway apunta directamente al endpoint correspondiente.

## Conclusiones

1. Eureka centraliza el registro y descubrimiento de servicios, evitando fijar sus direcciones dentro del Gateway.
2. El API Gateway ofrece un punto de entrada estable para el cliente y oculta los puertos internos de los microservicios.
3. Las rutas `lb://` permiten resolver el nombre lógico del servicio y distribuir solicitudes entre instancias disponibles.
4. Incorporar `cliente-service` y `pedido-service` requiere registrar cada aplicación en Eureka y agregar su ruta en la configuración del Gateway.
5. La separación por servicios facilita escalar y desplegar componentes por separado; en un entorno real el Gateway también debe configurarse con autenticación, límites de tráfico y observabilidad.

## Referencias de compatibilidad

- [Spring Cloud: compatibilidad de versiones](https://spring.io/projects/spring-cloud/)
- [Spring Cloud Gateway: rutas desde DiscoveryClient](https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway-server-webflux/the-discoveryclient-route-definition-locator.html)
