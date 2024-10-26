package com.maker.pacemaker.ui.viewmodel.main.details

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.maker.pacemaker.ui.viewmodel.main.MainBaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class MainProblemSolveScreenViewModel @Inject constructor(
    private val base: MainBaseViewModel,
) : ViewModel() {

    val baseViewModel = base

    // 오늘의 문제                                           정답 ,   문제 설명
    private val _todayProblems = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val todayProblems = _todayProblems

    private val _problemHints = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val problemHints = _problemHints


    private val _todaySolvedCount = MutableStateFlow(baseViewModel.baseViewModel.sharedPreferences.getInt("todaySolvedCount", 0))
    val todaySolvedCount: MutableStateFlow<Int> get() = _todaySolvedCount

    private val _answer = MutableStateFlow("")
    val answer = _answer

    private val _wrongCnt = MutableStateFlow(0)
    val wrongCnt = _wrongCnt

    init {
        _todayProblems.value = listOf(
            Pair("단일 책임 원칙(SRP)", "객체 지향의 SOLID 원칙 중 \"클래스는 단 하나의 목적을 가져야 하며, 변화의 이유가 하나만 있어야 한다\"는 원칙은 무엇일까요?"),
            Pair("개방-폐쇄 원칙(OCP)", "객체 지향의 SOLID 원칙 중 \"소프트웨어 요소는 확장에는 열려 있으나 변경에는 닫혀 있어야 한다\"는 원칙은 무엇일까요?"),
            Pair("리스코프 치환 원칙(LSP)", "객체 지향의 SOLID 원칙 중 \"서브타입은 반드시 자신의 기반 타입으로 교체할 수 있어야 한다\"는 원칙은 무엇일까요?"),
            Pair("인터페이스 분리 원칙(ISP)", "객체 지향의 SOLID 원칙 중 \"클라이언트는 자신이 사용하지 않는 인터페이스에 의존하지 않아야 한다\"는 원칙은 무엇일까요?"),
            Pair("의존 역전 원칙(DIP)", "객체 지향의 SOLID 원칙 중 \"고수준 모듈은 저수준 모듈에 의존해서는 안 되며, 둘 다 추상화에 의존해야 한다\"는 원칙은 무엇일까요?"),
            Pair("캡슐화(Encapsulation)", "객체 지향 프로그래밍의 개념 중 \"객체의 데이터와 행위를 하나로 묶고, 외부로부터 내부의 세부 사항을 숨기는 것\"을 무엇이라고 할까요?"),
            Pair("추상화(Abstraction)", "객체 지향 프로그래밍의 개념 중 \"복잡한 시스템을 단순하게 표현하기 위해 필수적 특성만을 추려내는 과정\"을 무엇이라고 할까요?"),
            Pair("상속(Inheritance)", "객체 지향 프로그래밍의 개념 중 \"기존 클래스의 속성과 메서드를 새로운 클래스에서 물려받아 사용하는 것\"을 무엇이라고 할까요?"),
            Pair("다형성(Polymorphism)", "객체 지향 프로그래밍의 개념 중 \"동일한 이름의 메서드가 서로 다른 동작을 할 수 있도록 하는 성질\"을 무엇이라고 할까요?"),
            Pair("의존성 주입(Dependency Injection)", "디자인 패턴 중 \"객체의 의존성을 외부에서 주입함으로써, 객체 간 결합도를 낮추고 유연성을 높이는 방식\"을 무엇이라고 할까요?")
        )

        _problemHints.value = listOf(
            "단일 책임 원칙(SRP)" to listOf("각 클래스는 하나의 기능만을 책임져야 합니다.", "변경의 이유는 하나만 존재해야 합니다.", "클래스를 작고 명확하게 유지합니다."),
            "개방-폐쇄 원칙(OCP)" to listOf("코드는 수정하지 않고도 기능을 확장할 수 있어야 합니다.", "기존 코드는 검증된 대로 유지됩니다.", "확장 가능하게 설계하는 것이 핵심입니다."),
            "리스코프 치환 원칙(LSP)" to listOf("서브클래스는 기반 클래스의 역할을 대체할 수 있어야 합니다.", "상위 클래스와 호환되지 않는 동작을 피해야 합니다.", "기반 타입의 역할이 보장되어야 합니다."),
            "인터페이스 분리 원칙(ISP)" to listOf("사용자가 필요하지 않은 기능에는 의존하지 않도록 합니다.", "작고 구체적인 인터페이스를 제공합니다.", "클라이언트가 필요하지 않은 기능을 포함하지 않습니다."),
            "의존 역전 원칙(DIP)" to listOf("상위 모듈은 하위 모듈에 의존하지 않습니다.", "구현이 아닌 추상화에 의존합니다.", "유연성을 제공하는 설계 원칙입니다."),
            "캡슐화(Encapsulation)" to listOf("데이터와 메서드를 하나의 단위로 묶습니다.", "내부 구현을 외부에 숨깁니다.", "정보 은닉이 핵심입니다."),
            "추상화(Abstraction)" to listOf("복잡한 시스템을 단순화하여 보여줍니다.", "핵심적인 기능만 드러냅니다.", "세부 사항을 감추는 것이 특징입니다."),
            "상속(Inheritance)" to listOf("기존 클래스의 기능을 재사용합니다.", "상위 클래스를 확장합니다.", "공통 기능을 자식 클래스에서 활용할 수 있습니다."),
            "다형성(Polymorphism)" to listOf("동일한 메서드 이름이 다르게 동작할 수 있습니다.", "서브클래스에서 메서드를 재정의합니다.", "유연한 코드 작성을 가능하게 합니다."),
            "의존성 주입(Dependency Injection)" to listOf("의존성을 외부에서 주입받아 결합도를 낮춥니다.", "객체 간 관계를 동적으로 구성합니다.", "테스트 용이성이 향상됩니다.")
        ).toMap()
    }

    fun onSubmit() {
        // 정답 제출, 서버 통신

        // 정답
        if (todayProblems.value[todaySolvedCount.value].first == _answer.value) {
            // 정답인 경우
            // 다음 문제로 이동
            _todaySolvedCount.value += 1
            _answer.value = ""
            _wrongCnt.value = 0
        } else {
            // 오답인 경우
            // 사용자에게 오답임을 알림
            baseViewModel.baseViewModel.triggerVibration()
            baseViewModel.baseViewModel.triggerToast("오답입니다. 다시 시도해주세요.")

            // 추가로 힌트 개방
            if (_wrongCnt.value < 3) _wrongCnt.value += 1
        }
        // 오답
    }

}