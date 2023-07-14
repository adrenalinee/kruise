nestedView(projectName) {
    columns {
        status()
        weather()
    }
    configure { view ->
        view / defaultView("Action")
    }
    views {
        listView(" All") {
            jobs {
                regex("${projectName}\\..*")
            }
            columns {
                status()
                weather()
                name()
                lastSuccess()
                lastFailure()
                lastDuration()
                buildButton()
            }
        }
        listView("Action") {
            jobs {
                regex("${projectName}\\.action\\..*")
            }
            columns {
                status()
                weather()
                name()
                lastSuccess()
                lastFailure()
                lastDuration()
                buildButton()
            }
        }
        listView("pipeline.${projectRepositoryBranch}") {
            jobs {
                regex("${projectName}\\.pipeline\\.${projectRepositoryBranch}\\..*")
            }
            columns {
                status()
                weather()
                name()
                lastSuccess()
                lastFailure()
                lastDuration()
                buildButton()
            }
        }
    }
}
