# Kruise

Kruise 는 Kubernetes 위에서 CI/CD 파이프라인을 자동화하는 도구로, Jenkins 를 통해 Gradle 빌드와 Docker 이미지 생성/푸시를 수행하고 Argo CD 에 Helm 차트로 배포를 생성합니다.

## Information
- 빌드/배포 환경: Kubernetes
- 빌드/배포 방법: Jenkins Job 실행
- 빌드 방식: Gradle, Docker image build/push
- 배포 방식: Argo CD + Helm chart

Jenkins Job 을 통해 프로젝트를 빌드한 뒤 Argo CD 에 Application 을 생성하고 배포까지 완료합니다.

## Prerequisites
- Helm CLI 가 설치되어 있어야 합니다.
- Jenkins 와 Argo CD 를 설치할 Kubernetes 클러스터에 접근 가능한 kubeconfig.
- 컨테이너 레지스트리, 소스 저장소, Argo CD 접속용 자격 증명.

## How to install

### 1) Helm 으로 Argo CD, Jenkins 설치
```shell
helm repo add argo https://argoproj.github.io/argo-helm
helm repo add jenkins https://charts.jenkins.io
helm dependency update ./helm
helm install argo-cd argo/argo-cd -f helm/values-argocd.yaml
helm install jenkins jenkins/jenkins -f helm/values-jenkins.yaml
```

### 2) Jenkins 에서 `kruise.init` Job 실행
사전 준비: Kruise 소스 저장소 접근용 Credential 생성
- Kind: Username with password
- GitHub Personal Access Token 사용 (repo 권한 필요)

### 3) 테스트 애플리케이션 배포용 seed Job 실행
필요 Credential
1. 애플리케이션 소스 저장소 접근용
   - Kind: Username with password
   - GitHub Personal Access Token (repo 권한)
2. 빌드된 이미지 push 용 컨테이너 레지스트리 인증
   - Kind: Username with password
3. Kubernetes 배포를 위한 Argo CD 인증
   - Kind: Username with password
   - Argo CD Access Token 사용

#### Argo CD Access Token 생성 예시
```shell
argocd login <ARGOCD_SERVER> --username admin --password <ADMIN_PASSWORD>
argocd account generate-token --account kruise-admin
```

## Global setting

### $KUBECONFIG
Helm chart 가 설치될 클러스터를 지정하는 환경 변수입니다. `helm` 명령 실행 전에 설정해 주세요.

> https://helm.sh/docs/helm/helm/#helm

```shell
export KUBECONFIG={kubeconfig yaml 파일 경로}
echo $KUBECONFIG
```
