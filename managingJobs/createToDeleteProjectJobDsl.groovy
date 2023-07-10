job("frodo:delete-project-jobs") {
    description("frodo seed job 으로 생성한 job 들을 삭제 합니다. projectName 단위로 삭제합니다.")
    parameters {
        credentialsParam("frodoRepositoryCredential") {
            type("com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")
            defaultValue(frodoRepositoryCredential)
            description("빌드 스크립트를 다운받을때 사용할 인증 token 을 지정하세요.")
        }
        stringParam {
            name("frodoRepositoryUrl")
            defaultValue(frodoRepositoryUrl)
            description("빌드 스크립트를 가져올 git 주소입니다.")
            trim(true)
        }
        stringParam {
            name("frodoBranch")
            defaultValue(frodoBranch)
            description("빌드 스크립트를 가져올 branch 입니다.")
            trim(true)
        }
        stringParam {
            name("projectName")
//            defaultValue("hello-world")
            description("삭제 할 프로젝트 이름입니다. 다른 프로젝트를 지우지 않도록 주의해주세요.")
            trim(true)
        }
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(frodoRepositoryUrl)
                        credentials(frodoCredential)
                    }
                    branch(frodoBranch)
                    scriptPath("jenkinsfile/delete-project-jobs.Jenkinsfile")
                }
            }
        }
    }
}
