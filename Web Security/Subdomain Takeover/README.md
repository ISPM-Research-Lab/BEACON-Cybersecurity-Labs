# [BEACON Labs - Web Security: Subdomain Takeover Attack Lab]

## Type

Web Security

## Overview

Attacks on related subdomains have received limited attention within the research community, though pose a significantly additional power over traditional web attacks. An attacker can take control over sibling domains which provide the ability to abuse many territories of web application security, such as: cookies, CSP, CORS, postMessage, domain relaxation, and more.

In this lab, we explore performing a subdomain takeover through the use of dangling DNS records, where subdomains reference empty third-party resources. Specifically, we will make use of Amazon’s DNS provider Route53 to host a set of targetable domains where we can perform real-world subdomain takeover attacks. Each subdomain will make use of a various set of external resources.

## Lab Materials

- [Student Lab Materials](/Web%20Security/Subdomain%20Takeover/Student%20Lab%20Materials/):
    - [Instruction Document](/Web%20Security/Subdomain%20Takeover/Student%20Lab%20Materials/Beacon_WS_DomainTakeoverAttack_Lab.pdf)
    - [Setup Document](/Web%20Security/Subdomain%20Takeover/Student%20Lab%20Materials/Beacon_WS_DomainTakeoverAttack_Lab_Setup.pdf)
    - [Report Template](/Web%20Security/Subdomain%20Takeover/Student%20Lab%20Materials/Lab_Report_Template.docx)
    - [Subdomain Takeover VM](https://drive.google.com/file/d/16-dKCAcqK_0KAH4ry7XoDgkttpSeVxFm/view?usp=sharing)
- [Instructor Lab Materials](/Web%20Security/Subdomain%20Takeover/Instructor%20Lab%20Materials/):
    - [Instructor Setup](/Web%20Security/Subdomain%20Takeover/Instructor%20Lab%20Materials/[WS]%20Subdomain%20Takeover%20-%20TEACHER%20Setup.docx)
    - [Instructor Manual](/Web%20Security/Subdomain%20Takeover/Instructor%20Lab%20Materials/[WS]%20Subdomain%20Takeover%20-%20TEACHER%20Lab%20Report.docx)
    - [LaTeX](/Web%20Security/Subdomain%20Takeover/Instructor%20Lab%20Materials/LaTeX/):
        - [Instruction Document](/Web%20Security/Subdomain%20Takeover/Instructor%20Lab%20Materials/LaTeX/Instruction-Document/)
        - [Setup Document](/Web%20Security/Subdomain%20Takeover/Instructor%20Lab%20Materials/LaTeX/Setup/)

## Source Paper

This lab is based on a paper from [the 30th USENIX Security Symposium](https://www.google.com/url?q=https%3A%2F%2Fwww.usenix.org%2Fsystem%2Ffiles%2Fsec21-squarcina.pdf&sa=D).

For additional information, visit [the USENIX website](https://www.google.com/url?q=https%3A%2F%2Fwww.usenix.org%2Fconference%2Fusenixsecurity21%2Fpresentation%2Fsquarcina&sa=D).

## Complementary Materials

![Subdomain Takeover video by Gunnar Vittrup.](https://drive.google.com/file/d/1AMzj18lK_GFgS5IlnMBdHWSNi71WXwnS/view?usp=sharing)

## Suggested Time

- Supervised (guided lab session): **1.15 hours**
- Unsupervised (take-home project): **0.5 weeks**
- Level of Difficulty: 1-2

## Feedback

If you have any feedback to provide on this lab please [let us know](https://docs.google.com/forms/d/1ERz-IhIprUOmSpoK3ARNiLZ9Z9JhZgCl4HE51Lb4rDs/edit?usp=sharing).
