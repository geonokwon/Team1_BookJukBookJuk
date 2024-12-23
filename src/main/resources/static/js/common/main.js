
(function() {
	"use strict";

	/**
	 * Easy selector helper function
	 */
	const select = (el, all = false) => {
		el = el.trim()
		if (all) {
			return [...document.querySelectorAll(el)]
		} else {
			return document.querySelector(el)
		}
	}

	/**
	 * Easy event listener function
	 */
	const on = (type, el, listener, all = false) => {
		if (all) {
			select(el, all).forEach(e => e.addEventListener(type, listener))
		} else {
			select(el, all).addEventListener(type, listener)
		}
	}

	/**
	 * Easy on scroll event listener 
	 */
	const onscroll = (el, listener) => {
		el.addEventListener('scroll', listener)
	}

	/**
	   * Sidebar toggle
	   */
	// 기본적으로 사이드바를 닫은 상태로 시작
	document.body.classList.add('toggle-sidebar'); // 초기 상태

	if (select('.toggle-sidebar-btn')) {
		on('click', '.toggle-sidebar-btn', function(e) {
			select('body').classList.toggle('toggle-sidebar');
		});
	}
	/**
	 * Navbar links active state on scroll
	 */
	let navbarlinks = select('#navbar .scrollto', true)
	const navbarlinksActive = () => {
		let position = window.scrollY + 200
		navbarlinks.forEach(navbarlink => {
			if (!navbarlink.hash) return
			let section = select(navbarlink.hash)
			if (!section) return
			if (position >= section.offsetTop && position <= (section.offsetTop + section.offsetHeight)) {
				navbarlink.classList.add('active')
			} else {
				navbarlink.classList.remove('active')
			}
		})
	}
	window.addEventListener('load', navbarlinksActive)
	onscroll(document, navbarlinksActive)

	/**
	 * Toggle .header-scrolled class to #header when page is scrolled
	 */
	let selectHeader = select('#header')
	if (selectHeader) {
		const headerScrolled = () => {
			if (window.scrollY > 100) {
				selectHeader.classList.add('header-scrolled')
			} else {
				selectHeader.classList.remove('header-scrolled')
			}
		}
		window.addEventListener('load', headerScrolled)
		onscroll(document, headerScrolled)
	}
	
	/**
	 * Sidebar unread notification count
	 */
	const updateUnreadNotifications = () => {
		const unreadCount = localStorage.getItem('unreadCount'); // 로컬 스토리지에서 값 가져오기
		const notificationCountEl = select('#notification-count'); // 사이드바의 알림 표시 요소 선택

		if (unreadCount !== null && notificationCountEl) {
			if (parseInt(unreadCount) > 0) {
				notificationCountEl.textContent = unreadCount; // 알림 개수 업데이트
				notificationCountEl.style.display = 'inline'; // 보이게 설정
			} else {
				notificationCountEl.style.display = 'none'; // 0개면 숨김
			}
		} else if (notificationCountEl) {
			notificationCountEl.style.display = 'none'; // 데이터 없으면 숨김
		}
	}
	
	window.addEventListener('load', updateUnreadNotifications);


})();