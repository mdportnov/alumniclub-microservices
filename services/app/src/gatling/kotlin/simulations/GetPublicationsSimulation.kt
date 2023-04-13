package simulations

import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.gatling.javaapi.http.HttpDsl.status
import io.gatling.javaapi.http.HttpProtocolBuilder
import java.time.Duration

class GetPublicationsSimulation : Simulation() {
    private val httpConf: HttpProtocolBuilder = http
        .baseUrl("http://localhost:8081/api/v1/public")
        .acceptHeader("application/json")

    private val scn = scenario("GetPublications")
        .repeat(1, "n").on(
            exec(
                http("GetAllPublications")
                    .get("/feed/publication")
                    .check(status().`is`(200))
            )
                .exec(
                    http("GetPublicationById")
//                        .get("/feed/publication/id/08cd222d-36e4-4c1e-bc4f-beae262337d1")
                        .get("/feed/publication/podvedeni-itogi-konkursa-na-sozdanie-originalnogo-suvenira-dlya-vipusknikov-i-partnyorov-niyau-mifi")
                        .check(status().`is`(200))
                )
        )

    init {
        setUp(
            scn.injectClosed(
                constantConcurrentUsers(100).during(Duration.ofSeconds(3))
            )
        ).protocols(httpConf)
    }
}
