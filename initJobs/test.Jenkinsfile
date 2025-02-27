final Integer idleMinutes = 60
final Integer instanceCap = 5

def test = params.test

podTemplate(
    inheritFrom: 'jenkins-agent-default',
    name: "jenkins-agent-default",
    label: "jenkins-agent-default",
    nodeUsageMode: "EXCLUSIVE", // label 이 일치하는 job 에서만 사용됨.
    idleMinutes: idleMinutes, //대기시간(대시시간동안 다른 job 실행가능).
    instanceCap: instanceCap //최대 생성가능한 동일 스팩 팟 갯수,
) {
    node("jenkins-agent-default") {
        stage("Checkout kruise") {
            git(url: kruiseRepositoryUrl, branch: kruiseBranch, credentialsId: kruiseRepositoryCredential)
        }
        stage("test") {
            test()
        }
    }
}

def test() {
    println(test)
    if (test) {
        println("!!!!")
    }
}