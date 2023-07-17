//gradle 기반 project 생성
pipelineJob("frodo.action.create-project-gradle") {
    description("frodo seed job 으로 생성한 job 들을 삭제 합니다. projectName 단위로 삭제합니다.")
    parameters {
//        credentialsParam("frodoRepositoryCredential") {
//            type("com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")
//            defaultValue(frodoRepositoryCredential)
//            description("빌드 스크립트를 다운받을때 사용할 인증 token 을 지정하세요.")
//        }
//        stringParam {
//            name("frodoRepositoryUrl")
//            defaultValue(frodoRepositoryUrl)
//            description("빌드 스크립트를 가져올 git 주소입니다.")
//            trim(true)
//        }
//        stringParam {
//            name("frodoBranch")
//            defaultValue(frodoBranch)
//            description("빌드 스크립트를 가져올 branch 입니다.")
//            trim(true)
//        }
        stringParam {
            name("projectName")
            defaultValue("hello-world")
            description("생성 할 프로젝트 이름입니다. 다른 프로젝트와 이름이 겹치지 않게 주의해주세요.")
            trim(true)
        }
        stringParam {
            name("projectRepositoryUrl")
            defaultValue("https://github.com/adrenalinee/hello-world-kotlin.git")
            description("빌드를 수행할 git 주소입니다.")
            trim(true)
        }
        stringParam {
            name("projectRepositoryBranch")
            defaultValue("develop")
            description("빌드를 수행할 기본 branch 입니다.")
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
        choiceParam {
            name("jdkVersion")
            choices(["17", "20"])
            description("build 를 진행할 jdk 의 버전을 지정합니다.")
        }
        stringParam {
            name("imagePath")
            defaultValue("docker.io/adrenalinee/hello-world-kotlin")
            description("pushImage = true 일 경우에 필수값입니다. image push 할 주소 입니다. 프로토콜을 제외한 경로 입니다. ex) domain/owner/repo")
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
                    scriptPath("jenkinsfile/create-project-gradle.Jenkinsfile")
                }
            }
        }
    }
}
