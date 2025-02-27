//gradle 기반 build project 생성
pipelineJob("kruise.action.create-project-gradle") {
    description("gradle build 후에 image build and push 하는 seed job 을 생성합니다.")
    parameters {
        stringParam {
            name("projectName")
            description("생성 할 프로젝트 이름입니다. 다른 프로젝트와 이름이 겹치지 않게 주의해주세요. 덮어쓰기가 될 수 있습니다. ex: hello-world")
            trim(true)
        }
        stringParam {
            name("projectRepositoryUrl")
            description("빌드를 수행할 소스의 git 주소입니다. ex: https://github.com/adrenalinee/hello-world-kotlin.git")
            trim(true)
        }
        stringParam {
            name("projectRepositoryBranch")
            defaultValue("develop")
            description("빌드를 수행할 branch 입니다. 생성된 argocd application 이름에 branch 명이 포함됩니다.")
            trim(true)
        }
        stringParam {
            name("clusterName")
            description("argocd application 을 배포할 클러스터 이름입니다. kruise-argocd 에 클러스터 등록할때 지정한 이름입니다. kruise-argocd 에서 사용가능한 클러스터 이름을 확인할 수 있습니다. ex: in-cluster")
        }
        stringParam {
            name("phase")
            description("argocd application, k8d resource name 의 뒤에 붙을 접미사. (optional) 같은 project 안에서 배포단위별로 추가적인 접미사를 지정할 수 있습니다. ex: dev, cbt, prod")
        }
        stringParam {
            name("imagePath")
            description("image push 할 주소 입니다. 프로토콜을 제외한 경로 입니다. ex: TODO")
            trim(true)
        }
        choiceParam {
            name("helmChartName")
            choices(["kruise-standard-server"])
            description("kruise 에 정의된 helm chart 중에 선택합니다.")
        }
        textParam {
            name("helmChartValues")
            description("위에서 선택한 helmChart 에서 변경할 values 를 지정합니다. 배포할 image 의 repository 와 tag 는 kruise 에서 관리합니다.")
            defaultValue("""replicaCount=1
ingress.enabled=true
ingress.className=nginx
ingress.hosts[0].host=
ingress.hosts[0].paths[0].path=/
ingress.hosts[0].paths[0].pathType=ImplementationSpecific
ingress.tls[0].secretName=
ingress.tls[0].hosts[0]=
serviceMonitor.enabled=true
serviceMonitor.labels.release=prometheus
filebeat.enabled=true
filebeat.logFilePath=/opt/app/logs
filebeat.logFileName=
filebeat.outputElasticsearchHost=
containerEnv.SPRING_PROFILES_ACTIVE=dev""")
        }
        booleanParam {
            name("override")
            description("기존에 이미 같은 이름으로 argocd application 이 만들어져 있을때, 덮어쓸지 여부입니다. ")
        }
        choiceParam {
            name("jdkVersion")
            choices(["17", "21"])
            description("build 를 진행할 jdk 의 버전을 지정합니다.")
        }
        textParam {
            name("gradleBuildCommand")
            defaultValue("build")
        }
        stringParam {
            name("proxy")
            defaultValue("")
            description("proxy 서버 주소입니다.")
            trim(true)
        }
        stringParam {
            name("noProxy")
            defaultValue("")
            description("proxy 서버를 사용하지 않을 주소(도메인, ip) 목록입니다. 여러개의 주소는 ','  로 구별합니다.")
            trim(true)
        }

        credentialsParam("projectRepositoryCredential") {
            type("com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")
            defaultValue(kruiseRepositoryCredential)
            description("project repository 인증용 계정 을 지정하세요.")
        }
        credentialsParam("containerRegistryCredential") {
            type("com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")
            defaultValue(containerRegistryCredential)
            description("containerRegistry 인증용 계정을 지정하세요.")
        }
        credentialsParam("kruiseRepositoryCredential") {
            type("com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl")
            defaultValue(kruiseRepositoryCredential)
            description("빌드 스크립트를 다운받을때 사용할 인증 token 을 지정하세요.")
        }
        stringParam {
            name("kruiseRepositoryUrl")
            defaultValue(kruiseRepositoryUrl)
            description("빌드 스크립트를 가져올 git 주소입니다.")
            trim(true)
        }
        stringParam {
            name("kruiseBranch")
            defaultValue(kruiseBranch)
            description("빌드 스크립트를 가져올 branch 입니다.")
            trim(true)
        }
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url(kruiseRepositoryUrl)
                        credentials(kruiseRepositoryCredential)
                    }
                    branch(kruiseBranch)
                    scriptPath("initJobs/actionCreateProjectGradle.Jenkinsfile")
                }
            }
        }
    }
}