package simulations

import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status
import io.gatling.javaapi.http.HttpProtocolBuilder
import java.time.Duration

class GetEventSimulation : Simulation() {
    private val httpConf: HttpProtocolBuilder = http
//        .baseUrl("http://localhost:8081/api/v1/public")
        .baseUrl("https://alumniclub.mephi.ru/api/v1/public")
        .acceptHeader("application/json")

    private val scn = scenario("GetEvents")
        .repeat(1, "n").on(
//            exec(
//                http("GetEventPreview")
//                    .get("/feed/event/preview")
//                    .check(status().`is`(200))
//            )
            exec(
                http("GetEventById1")
                    .get("/feed/event/27e3c6ef-cf70-4c23-b05d-f04cc3fb47ab")
                    .check(status().`is`(200))
            )
//                .exec(
//                    http("GetEventById2")
//                        .get("/feed/event/4561908c-923f-4d25-b7a9-e23a365d9141")
//                        .check(status().`is`(200))
//                )
        )

    init {
        setUp(
            scn.injectClosed(
                constantConcurrentUsers(100).during(Duration.ofSeconds(3))
            )
        ).protocols(httpConf)
    }
}
