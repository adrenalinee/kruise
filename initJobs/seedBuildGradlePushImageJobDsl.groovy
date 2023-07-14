job("frodo.managed.seed.build-gradle-push-image") {
    description("frodo 를 위한 build gradle push image job 을 생성해주는 seed job 입니다.")
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
            defaultValue("hello-world")
            description("프로젝트 이름입니다. 생성되는 job 들의 prefix 가 됩니다. 주의: 다른 프로젝트와 중복되면 안됩니다. 해당 프로젝트를 덮어쓰게 됩니다.")
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
    scm {
        git {
            branch('$frodoBranch')
            remote {
                credentials('$frodoRepositoryCredential')
                url('$frodoRepositoryUrl')
            }
        }
    }
    steps {
        jobDsl {
            targets(
                "seedJobs/createBuildGradlePushImageJobDsl.groovy\n" +
                "seedJobs/createView.groovy"
            )
            sandbox(true)
        }
    }
}
