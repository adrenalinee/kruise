nestedView(projectName) {
    columns {
        status()
        weather()
    }
    views {
        listView(projectRepositoryBranch) {
            jobs {
                regex("${projectName}-${projectRepositoryBranch}-.*")
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
