# MapleStory 게임 프로젝트

Java 기반의 2D 멀티플레이어 횡스크롤 액션 게임입니다. 클라이언트-서버 아키텍처를 사용하여 여러 플레이어가 동시에 접속하여 플레이할 수 있습니다.

## 목차
- [프로젝트 구조](#프로젝트-구조)
- [주요 기능](#주요-기능)
- [실행 방법](#실행-방법)
- [게임 조작법](#게임-조작법)
- [아키텍처 설명](#아키텍처-설명)

## 프로젝트 구조

```
MapleStory/
├── src/
│   ├── client/              # 클라이언트 패키지
│   │   ├── GameClient.java          # 클라이언트 진입점
│   │   ├── GameFrame.java           # 메인 게임 윈도우
│   │   ├── GamePanel.java           # 게임 렌더링 패널 및 입력 처리
│   │   ├── GameRenderer.java        # 게임 요소 렌더링 로직
│   │   ├── GameStateParser.java     # 서버 상태 파싱
│   │   ├── NetworkHandler.java      # 서버 통신 관리
│   │   ├── PlayerInputHandler.java  # 키보드 입력 처리
│   │   └── SpriteManager.java       # 스프라이트 리소스 관리
│   │
│   ├── server/              # 서버 패키지
│   │   ├── GameServer.java          # 서버 진입점
│   │   ├── ClientHandler.java       # 개별 클라이언트 연결 처리
│   │   ├── GameLoop.java            # 게임 루프 (60 FPS)
│   │   ├── GameState.java           # 게임 상태 관리
│   │   ├── GameStateSerializer.java # 게임 상태 JSON 직렬화
│   │   └── MessageParser.java       # 클라이언트 메시지 파싱
│   │
│   ├── common/              # 공통 패키지
│   │   ├── Player.java              # 플레이어 엔티티
│   │   ├── Monster.java             # 몬스터 엔티티
│   │   │
│   │   ├── dto/                     # 데이터 전송 객체
│   │   │   ├── GamePacket.java
│   │   │   ├── GameStateDTO.java
│   │   │   ├── PlayerUpdateDTO.java
│   │   │   └── SkillDTO.java
│   │   │
│   │   ├── enums/                   # 열거형
│   │   │   └── Direction.java       # 방향 (LEFT, RIGHT, UP, DOWN)
│   │   │
│   │   └── skills/                  # 스킬 시스템
│   │       ├── Skill.java           # 스킬 추상 클래스
│   │       └── Skill1.java          # 첫 번째 스킬 구현
│   │
│   └── resource/            # 리소스 처리 유틸리티
│       ├── GifBackgroundRemover.java
│       └── GifUtil.java
│
├── img/                     # 게임 리소스
│   ├── character.jpeg       # 캐릭터 스프라이트
│   ├── monster.gif          # 몬스터 스프라이트
│   ├── skill1.gif           # 스킬 이펙트
│   ├── skill1_transparent.gif
│   └── 헤네시스.png          # 배경 이미지
│
└── out/                     # 컴파일된 클래스 파일

```

## 주요 기능

### 플레이어 시스템
- 좌우 이동
- 점프 (중력 및 물리 적용)
- 방향 전환
- 실시간 위치 동기화

### 스킬 시스템
- 방향성 발사체 스킬
- 플레이어 방향에 따른 스킬 발사
- 스킬 범위 및 속도 설정
- GIF 애니메이션 지원

### 네트워크
- TCP 소켓 기반 통신
- JSON 메시지 프로토콜
- 실시간 게임 상태 브로드캐스팅
- 멀티플레이어 지원

### 렌더링
- 60 FPS 게임 루프
- Swing 기반 그래픽
- 스프라이트 애니메이션
- 배경 이미지 렌더링

## 실행 방법

### 1. 재컴파일
```bash
rm -rf out/* && find src -name "*.java" -print0 | xargs -0 javac -d out -cp src
```

### 2. 서버 실행
```bash
cd out
java server.GameServer
```

### 3. 클라이언트 실행 (별도 터미널)
```bash
cd out
java client.GameClient
```

### 4. 다중 클라이언트 접속
여러 터미널에서 클라이언트를 실행하여 멀티플레이어 테스트 가능

## 게임 조작법

| 키 | 기능 |
|---|---|
| `←` | 왼쪽 이동 |
| `→` | 오른쪽 이동 |
| `SPACE` | 점프 |
| `Q` | 스킬 1 사용 |

## 아키텍처 설명

### 클라이언트-서버 통신 구조

```
[클라이언트]                    [서버]
    │                            │
    ├─ NetworkHandler ─────────→ ClientHandler
    │    (메시지 전송)            (메시지 수신 및 처리)
    │                            │
    │                            ├─ GameState
    │                            │   (게임 상태 관리)
    │                            │
    │                            └─ GameLoop
    ├─ GameStateParser ←─────────    (상태 업데이트 & 브로드캐스트)
    │    (상태 업데이트)
    │
    └─ GameRenderer
         (화면 렌더링)
```

### 메시지 프로토콜

#### WELCOME (서버 → 클라이언트)
```json
{"type":"WELCOME","payload":{"id":"player_abc123"}}
```

#### PLAYER_UPDATE (클라이언트 → 서버)
```json
{"type":"PLAYER_UPDATE","payload":{"x":100,"y":500,"state":"move","direction":"left"}}
```

#### SKILL_USE (클라이언트 → 서버)
```json
{"type":"SKILL_USE","payload":{"skillType":"skill1","direction":"right"}}
```

#### GAME_STATE (서버 → 클라이언트, 브로드캐스트)
```json
{
  "type":"GAME_STATE",
  "payload":{
    "players":[...],
    "monsters":[...],
    "skills":[...]
  }
}
```

### 게임 루프

**서버 사이드 (60 FPS)**
1. 몬스터 AI 업데이트
2. 스킬 이동 및 범위 체크
3. 게임 상태 직렬화
4. 모든 클라이언트에게 브로드캐스트

**클라이언트 사이드 (60 FPS)**
1. 로컬 점프 물리 계산
2. 키 입력 처리
3. 서버로부터 받은 상태로 동기화
4. 화면 렌더링

### 주요 클래스 설명

#### GameState
- 모든 플레이어, 몬스터, 스킬의 상태를 관리
- Thread-safe한 CopyOnWriteArrayList 사용
- 게임 오브젝트 추가/삭제/조회 API 제공

#### Skill (추상 클래스)
- 모든 스킬의 기본 동작 정의
- `update()`: 매 프레임 스킬 이동 및 범위 체크
- `loadResources()`: 스킬별 리소스 로딩 (추상 메서드)
- Direction에 따라 좌우로 이동

#### Direction (Enum)
- LEFT, RIGHT, UP, DOWN 방향 정의
- String ↔ Enum 변환 메서드 제공
- 기본값: RIGHT

## 기술 스택

- **언어**: Java
- **GUI**: Java Swing
- **네트워크**: Java Socket (TCP)
- **데이터 포맷**: JSON (수동 파싱)
- **동시성**: Thread, CopyOnWriteArrayList

## 향후 개선 사항

- [ ] 몬스터와의 충돌 감지
- [ ] 스킬-몬스터 충돌 및 데미지 시스템
- [ ] HP/MP 시스템
- [ ] 더 많은 스킬 추가
- [ ] 맵 경계 처리
- [ ] 플레이어 간 충돌 처리