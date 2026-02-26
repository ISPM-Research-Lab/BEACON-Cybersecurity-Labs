# [BEACON Labs - Mobile Security: Android State Inference Attack Lab](https://sites.google.com/boisestate.edu/beacon-labs/mobile-security-labs/android-state-inference-attack-lab)

## Type

Mobile Security Lab

## Overview

In the realm of the Android ecosystem, one relevant threat is posed by phishing attacks. Phishing attacks are particularly problematic for mobile platforms because they do not provide enough information for a user to reliably distinguish a legitimate app from a malicious app spoofing the UI of the legitimate one. A key factor that determines the success rate of a phishing attack is proper timing: The user is more prone to provide sensitive data (such as her passwords) if the malicious spoofed UI appears when the victim expects to interact with the target app. On Android, malware determines the right timing by mounting so-called state inference attacks, which can be used, for example, to infer the exact moment that the user started a target app and thus expects to interact with it. Even though Android app sandbox is designed to prevent these attacks, they are still possible by abusing vulnerable APIs that leak such sensitive information: the usual scenario is a malicious app that "polls" these vulnerable APIs, infers when a target app is about to be used by the user, and makes the spoofed UI appear on top of the screen at the right time.

## Lab Materials

- [Student Lab Materials](/Mobile%20Security/Android%20State%20Inference/Student%20Lab%20Materials/):
    - [Instruction Document](/Mobile%20Security/Android%20State%20Inference/Student%20Lab%20Materials/Beacon_MS_Android_SIA_Lab.pdf)
    - [Report Template](/Mobile%20Security/Android%20State%20Inference/Student%20Lab%20Materials/Lab_Report_Template.docx)
    - [Project Folder](https://drive.google.com/file/d/1ET-04AcvfmmpSW9RafcL5o5jcTqGdRW9/view?usp=drive_link)
    - **Android Studio**: The Android Studio IDE can be downloaded from the [official Android Developer website](https://developer.android.com/studio).
        - Alternatively, Android Studio can be downloaded from [JetBrains Toolbox](https://www.jetbrains.com/toolbox-app/).
        - If the Nexus 5X device or API 27 version doesn't show as a result, please download Android Studio Ladybug Feature Drop | 2024.2.2 Patch 2 from February 26, 2025 from the [Android Studio Download Archive](https://www.google.com/url?q=https%3A%2F%2Fdeveloper.android.com%2Fstudio%2Farchive&sa=D).
- [Instructor Lab Materials](/Mobile%20Security/Android%20State%20Inference/Instructor%20Lab%20Materials/):
    - [Instructor Manual](/Mobile%20Security/Android%20State%20Inference/Instructor%20Lab%20Materials/[SIA]%20Lab%20Report%20Teacher.docx)
    - [Instructor Solution Project](https://drive.google.com/file/d/1FqREHQErsRPbYBKCK9hYTcVrSyWm0koP/view?usp=sharing)
    - [LaTeX](/Mobile%20Security/Android%20State%20Inference/Instructor%20Lab%20Materials/LaTeX/)

## Source Paper

This lab is based on a [paper from the 2021 NDSS Symposium](https://www.ndss-symposium.org/wp-content/uploads/ndss2021_3B-5_24479_paper.pdf).

For additional information, visit the [NDSS website](https://www.ndss-symposium.org/ndss-paper/preventing-and-detecting-state-inference-attacks-on-android/).

## Complementary Materials

![Android State Inference Attack video by Gunnar Vittrup.](https://drive.google.com/file/d/1bNni30Uiblfd5FO4MRMZMccZ4AauL7OY/view?usp=sharing)

## Suggested Time

- Supervised (guided lab session): **5 hours**
- Unsupervised (take-home project): **2 weeks**
- Level of Difficulty: 3-4

## Feedback

If you have any feedback to provide on this lab please [let us know](https://docs.google.com/forms/d/1ERz-IhIprUOmSpoK3ARNiLZ9Z9JhZgCl4HE51Lb4rDs/edit?usp=sharing).
