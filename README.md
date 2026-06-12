# 📱 Gerenciador de Requisitos de Software

Aplicativo mobile Android desenvolvido para coleta e gerenciamento de requisitos funcionais e não funcionais de software em reuniões de levantamento de requisitos.

---

## 🚀 Sobre o Projeto

O **Gerenciador de Requisitos** é um aplicativo Android que permite equipes de desenvolvimento registrar, organizar e consultar requisitos de software de forma estruturada, diretamente do dispositivo móvel durante reuniões com clientes e stakeholders.

---

## ✨ Funcionalidades

### 🔐 Autenticação
- Cadastro de usuários com validação de e-mail e senha
- Tela de login com controle de acesso
- Logout seguro com limpeza de sessão

### 📁 Gestão de Projetos
- Cadastro de projetos com nome, data de início e data de entrega
- Link de documentação complementar (Google Drive, GitHub, etc.)
- Visualização do link em **WebView** integrada
- Listagem, edição e exclusão de projetos

### 📋 Gestão de Requisitos
- Cadastro de requisitos vinculados a projetos
- Descrição detalhada do requisito
- **Nível de importância** (1 a 5) via SeekBar
- **Nível de dificuldade** (1 a 5) via SeekBar
- **Tempo estimado** em horas/homem
- Data e hora de registro automáticos
- Listagem, edição e exclusão de requisitos

### 📍 Sensores do Dispositivo
- **GPS:** Captura automática da localização geográfica no momento do registro
- **Câmera:** Registro de até 2 fotos por requisito diretamente da câmera do dispositivo

### 🌐 WebView
- Visualização de documentação complementar diretamente no app
- Suporte a links do Google Drive, GitHub, Notion e qualquer URL

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Descrição |
|---|---|
| **Java** | Linguagem principal de desenvolvimento |
| **Android SDK** | API 21+ (Android 5.0 Lollipop) |
| **SQLite** | Banco de dados local com relacionamento entre tabelas |
| **RecyclerView** | Listagem dinâmica de projetos e requisitos |
| **WebView** | Visualização de documentação web |
| **FusedLocationProvider** | Captura de coordenadas GPS |
| **FileProvider** | Gerenciamento seguro de arquivos de foto |
| **Camera Intent** | Integração com câmera do dispositivo |
| **Material Components** | Componentes visuais modernos |
| **CardView** | Cards para exibição de itens |

---

## 🗄️ Modelagem do Banco de Dados

```
USUARIO
├── id (PK)
├── nome
├── email (UNIQUE)
└── senha

PROJETO
├── id (PK)
├── nome
├── data_inicio
├── data_entrega
└── link_documentacao

REQUISITO
├── id (PK)
├── projeto_id (FK → PROJETO)
├── descricao
├── data_registro
├── nivel_importancia (1-5)
├── nivel_dificuldade (1-5)
├── tempo_estimado
├── latitude
├── longitude
├── foto1
└── foto2
```

**Relacionamento:** 1 Projeto → N Requisitos

---

## 📁 Estrutura do Projeto

```
app/src/main/java/com/exemplo/requisitos/
├── LoginActivity.java              # Tela de login
├── CadastroUsuarioActivity.java    # Cadastro de usuários
├── MainActivity.java               # Tela principal
├── CadastroProjetoActivity.java    # Cadastro de projetos
├── EdicaoProjetoActivity.java      # Edição de projetos
├── ListaProjetosActivity.java      # Listagem de projetos
├── CadastroRequisitoActivity.java  # Cadastro de requisitos (GPS + Câmera)
├── EdicaoRequisitoActivity.java    # Edição de requisitos
├── ListaRequisitosActivity.java    # Listagem de requisitos
├── WebViewActivity.java            # Visualização de documentação
├── DatabaseHelper.java             # Gerenciamento do banco SQLite
├── Projeto.java                    # Modelo de dados - Projeto
├── Requisito.java                  # Modelo de dados - Requisito
├── ProjetoAdapter.java             # Adapter RecyclerView - Projetos
└── RequisitoAdapter.java           # Adapter RecyclerView - Requisitos
```

---

## ⚙️ Como Rodar o Projeto

### Pré-requisitos
- Android Studio Hedgehog ou superior
- JDK 8+
- Android SDK API 34
- Emulador ou dispositivo físico com Android 5.0+


```

### Permissões necessárias
O app solicitará as seguintes permissões em tempo de execução:
- `CAMERA` — para captura de fotos
- `ACCESS_FINE_LOCATION` — para captura de coordenadas GPS

---

## 📸 Telas do Aplicativo

| Tela | Descrição |
|---|---|
| Login | Autenticação com e-mail e senha |
| Cadastro de Usuário | Registro de novo usuário |
| Tela Principal | Menu com acesso às funcionalidades |
| Lista de Projetos | Projetos cadastrados com opções de ver, editar, excluir e abrir documentação |
| Cadastro de Projeto | Formulário com DatePicker e campo de link |
| Lista de Requisitos | Requisitos do projeto selecionado com GPS e opções de ação |
| Cadastro de Requisito | Formulário com SeekBars, GPS e câmera |
| WebView | Visualização da documentação do projeto |

---

## 🎓 Contexto Acadêmico

Projeto desenvolvido como atividade prática da disciplina de **Desenvolvimento de Aplicativos Mobile**, composto por 4 entregas progressivas:

| Entrega | Conteúdo |
|---|---|
| Entrega 1 | Diagrama ER + Telas de cadastro |
| Entrega 2 | Banco de dados SQLite + Listagens |
| Entrega 3 | Sensores: GPS e Câmera + Edição/Exclusão |
| Entrega 4 | Login/Autenticação + WebView + Link de documentação |

---

## 👨‍💻 Autor

**Samuel Caldeira**
---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
