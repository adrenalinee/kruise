//kruise 관련 job 을 뷰에 모아 놓음.
nestedView("kruise") {
    columns {
        status()
        weather()
    }
//    configure { view ->
//        view / defaultView("Action")
//    }
    views {
        listView(" All") {
            jobs {
                regex("kruise\\..*")
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
                regex("kruise.action\\..*")
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