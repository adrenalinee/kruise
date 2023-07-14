pipelineJob("frodo.delete.argocd-app") {
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
            description("프로젝트 이름입니다. 생성되는 job 들의 prefix 가 됩니다. 주의: 다른 프로젝트와 중복되면 안됩니다. 해당 프로젝트를 덮어쓰게 됩니다.")
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
                    scriptPath("jenkinsfile/delete-argocd-app.Jenkinsfile")
                }
            }
        }
    }
}
