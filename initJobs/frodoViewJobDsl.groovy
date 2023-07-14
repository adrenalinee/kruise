nestedView("frodo") {
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
                regex("frodo\\..*")
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
                regex("frodo.action\\..*")
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
