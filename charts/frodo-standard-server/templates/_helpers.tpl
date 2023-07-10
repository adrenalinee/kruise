{{/*
Expand the name of the chart.
*/}}
{{- define "frodo-standard-server.name" -}}
{{- default .Release.Name | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "frodo-standard-server.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "frodo-standard-server.labels" -}}
helm.sh/chart: {{ include "frodo-standard-server.chart" . }}
{{ include "frodo-standard-server.selectorLabels" . }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "frodo-standard-server.selectorLabels" -}}
app.kubernetes.io/name: {{ include "frodo-standard-server.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "frodo-standard-server.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "frodo-standard-server.name" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}
