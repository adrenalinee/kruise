# Frodo

ci/cd helper


# helm chart
charts 수정후 패키지 갱신을 위해 실행할 명령어

패키지 파일 생성
```shell
cd package
helm package ../charts/[챠트 경로]
```

(index.yaml 파일 있는 최상위 경로에서) index.yaml 파일 생성.
```shell
helm repo index .
```
