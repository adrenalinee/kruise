package plain

listView(projectName) {
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