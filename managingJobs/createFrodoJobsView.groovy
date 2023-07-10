listView("frodo-jobs") {
    jobs {
        regex("frodo-.*")
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
