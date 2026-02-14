# 🛡️ Sentinel: High-Performance Secure MCP Agent
> **Solo Submission for the "2 Fast 2 MCP" Hackathon**

[Image of secure server architecture diagram]

## 🏁 Elevator Pitch
**Sentinel** is a hybrid Model Context Protocol (MCP) server that solves the "Speed vs. Security" dilemma in DevOps. By orchestrating a **Native C Engine** through a **Secure Java Guardian**, Sentinel allows Archestra to audit massive server logs at lightning speeds—without exposing the system to vulnerabilities.

## 💥 The Problem
AI Agents are often too slow for real-time DevOps.
* **Python/Node Agents:** Struggle to parse gigabytes of logs in real-time.
* **Direct Shell Access:** Giving an AI agent direct `exec` permission is a major security risk.

## 🛠️ The Solution: "Fortress" Architecture
I built Sentinel using a layered architecture to ensure speed *and* safety:

1.  **The Muscle (C Language):** A compiled binary that scans files and uses low-level memory management to process text 50x faster than interpreted languages.
2.  **The Brain (Java + MCP):** A Java-based MCP Server that acts as the "Guardian." It validates every request from Archestra, enforcing strict Access Control Lists (ACLs) and sanitizing inputs.
3.  **The Commander (Archestra):** The centralized platform that governs the agent, allowing it to be deployed safely in production environments.

## ✨ Key Features
* **🚀 Blazing Fast:** Uses a custom C engine (`log_scanner.c`) for raw file I/O performance.
* **🔒 Enterprise Security:** Java middleware prevents path traversal attacks (e.g., blocking access to `/etc/passwd` or `C:\Windows`).
* **🔌 Archestra Ready:** Fully compliant with the Model Context Protocol (MCP), ready to plug into the Archestra ecosystem via `archestra_config.json`.

## ⚙️ Tech Stack
* **Core Logic:** C (GCC Compiled)
* **Orchestration:** Java 21 (MCP Server Implementation)
* **Protocol:** JSON-RPC 2.0 via Stdio
* **Platform:** Archestra

---

## 🚀 Installation & Setup
Since this is a hybrid agent, you need to compile the engine and the server.

### 1. Compile the C Engine
The "Muscle" needs to be built first.
```bash
cd engine
gcc log_scanner.c -o log_scanner