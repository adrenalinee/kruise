pipelineJob("frodo.managed.create-argocd-app") {
    description("argocd app 을 생성합니다.")
    parameters {
        stringParam {
            name("projectName")
            defaultValue("hello-world")
            description("프로젝트 이름입니다. 생성되는 job 들의 prefix 가 됩니다. 주의: 다른 프로젝트와 중복되면 안됩니다. 해당 프로젝트를 덮어쓰게 됩니다.")
            trim(true)
        }
        stringParam {
            name("helmChartRepositoryUrl")
            defaultValue("https://github.com/adrenalinee/frodo.git")
            description("helm chart git 주소입니다.")
            trim(true)
        }
        stringParam {
            name("helmChartBranch")
            defaultValue("master")
            description("helm chart 저장소의 branch 입니다.")
            trim(true)
        }
        stringParam {
            name("helmChartPath")
            defaultValue("charts/frodo-standard-server")
            description("helm chart 경로 입니다.")
            trim(true)
        }
        stringParam {
            name("imagePath")
            defaultValue("adrenalinee/hello-world-kotlin")
            description("container image 경로 입니다.")
            trim(true)
        }
        stringParam {
            name("imageTag")
            defaultValue("20230710-071748")
            description("container image tag 입니다.")
            trim(true)
        }
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(frodoRepositoryUrl)
                        credentials(frodoRepositoryCredential)
                    }
                    branch(frodoBranch)
                    scriptPath("jenkinsfile/create-argocd-app.Jenkinsfile")
                }
            }
        }
    }
}
