//package ru.mephi.alumniclub.app.service
//
//import org.junit.jupiter.api.*
//import org.junit.jupiter.api.Assertions.*
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.context.TestPropertySource
//import org.springframework.transaction.annotation.Transactional
//import ru.mephi.alumniclub.app.database.repository.user.RefreshTokenDao
//import ru.mephi.alumniclub.app.database.repository.user.UserDao
//import ru.mephi.alumniclub.app.database.repository.user.VerifyEmailTokenDao
//import ru.mephi.alumniclub.common.model.common.ApiError
//import ru.mephi.alumniclub.app.model.dto.auth.request.LoginRequest
//import ru.mephi.alumniclub.app.model.dto.auth.request.RefreshRequest
//import ru.mephi.alumniclub.app.model.enums.user.Role
//import ru.mephi.alumniclub.app.model.exceptions.auth.*
//import ru.mephi.alumniclub.app.model.exceptions.common.ResourceAlreadyExistsException
//import ru.mephi.alumniclub.app.service.helpers.UserHelper
//import ru.mephi.alumniclub.app.service.helpers.UserMock
//import ru.mephi.alumniclub.app.service.impl.auth.token.TokenManager
//import java.time.Instant
//import java.util.*
//
//@SpringBootTest
//@Transactional
//@TestPropertySource(locations = ["classpath:application-test.yml"])
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class IAuthServiceTest {
//
//    @Autowired
//    private lateinit var passwordEncoder: PasswordEncoder
//
//    @Autowired
//    private lateinit var userHelper: UserHelper
//
//    @Autowired
//    private lateinit var tokenManager: TokenManager
//
//    @Autowired
//    private lateinit var userDao: UserDao
//
//    @Autowired
//    private lateinit var verifyEmailTokenDao: VerifyEmailTokenDao
//
//    @Autowired
//    private lateinit var refreshTokenDao: RefreshTokenDao
//
//    @Autowired
//    private lateinit var authService: AuthService
//
//    @Autowired
//    private lateinit var verifyEmailService: VerifyEmailService
//
//    @Autowired
//    private lateinit var resetPasswordService: ResetPasswordService
//
//
//    @BeforeEach
//    fun prepareData() {
//        userHelper.toggleVerification(true)
//        cleanData()
//    }
//
//    @AfterEach
//    fun cleanData() = userHelper.deleteAll()
//
//    @Test
//    fun register() {
//        val request = userHelper.registrationRequest()
//        val response = authService.register(request)
//        assertEquals(request.email, response.email)
//    }
//
//    @Test
//    fun `register with existing email`() {
//        val request = userHelper.registrationRequest()
//        assertThrows<ResourceAlreadyExistsException> {
//            authService.register(request)
//            authService.register(request)
//        }
//    }
//
//    @Test
//    fun `register with LYCEUM role`() {
//        val request = userHelper.registrationRequest(roles = setOf(Role.LYCEUM))
//        val response = authService.register(request)
//        assertEquals(request.email, response.email)
//    }
//
//    @Test
//    fun `register with STUDENT role`() {
//        val request = userHelper.registrationRequest(roles = setOf(Role.STUDENT))
//        val response = authService.register(request)
//        assertEquals(request.email, response.email)
//    }
//
//    @Test
//    fun `register with ALUMNUS role`() {
//        val request = userHelper.registrationRequest(roles = setOf(Role.ALUMNUS))
//        val response = authService.register(request)
//        assertEquals(request.email, response.email)
//    }
//
//    @Test
//    fun login() {
//        userHelper.registerUser().also { verifyEmailService.deleteById(it.id) }
//        assertDoesNotThrow { authService.login(UserMock.loginRequest()) }
//    }
//
//    @Test
//    fun `login without email verification`() {
//        userHelper.registerUser()
//        assertThrows<EmailNotVerifiedException> { authService.login(UserMock.loginRequest()) }
//    }
//
//    @Test
//    fun `login admin`() {
//        userHelper.registerAdmin()
//        val request = UserMock.loginRequest()
//        assertDoesNotThrow { authService.loginModerator(request) }
//    }
//
//    @Test
//    fun `login admin without ADMIN role`() {
//        userHelper.registerUser().also { verifyEmailService.deleteById(it.id) }
//        val request = UserMock.loginRequest()
//        assertThrows<ApiError> { authService.loginModerator(request) }
//    }
//
//    @Test
//    fun `login with wrong password`() {
//        userHelper.registerUser().also { verifyEmailService.deleteById(it.id) }
//        val request = LoginRequest(UserMock.EMAIL.value, "wrong password", UserMock.FINGERPRINT.value)
//        assertThrows<ApiError> { authService.login(request) }
//    }
//
//    @Test
//    fun `login with wrong email`() {
//        userHelper.registerUser().also { verifyEmailService.deleteById(it.id) }
//        val request = LoginRequest("wrong@mail.ru", UserMock.PASSWORD.value, UserMock.FINGERPRINT.value)
//        assertThrows<ApiError> { authService.login(request) }
//    }
//
//    @Test
//    fun `verify email by invalid token`() {
//        assertThrows<ApiError> { verifyEmailService.verifyEmail("invalid") }
//    }
//
////    @Test
////    fun `register after verify token expiration`() {
////        val user = userHelper.registerUser()
////        verifyEmailTokenDao.findById(user.id).get().apply { createdAt = LocalDateTime.now().minusDays(2) }
////        userHelper.registerUser()
////        assertDoesNotThrow { authService.login(UserMock.loginRequest()) }
////    }
//
//    @Test
//    fun `get access token`() {
//        val user = userHelper.registerUser().also { verifyEmailService.deleteById(it.id) }
//        val token = authService.login(UserMock.loginRequest()).first
//        val principal = tokenManager.parseTokenPrincipal(token.accessToken)
//        assertEquals(principal.userId, user.id)
//    }
//
//    @Test
//    fun `refresh token`() {
//        val user = userHelper.registerUser().also { verifyEmailService.deleteById(it.id) }
//        val token = authService.login(UserMock.loginRequest()).second
//        val refreshRequest = RefreshRequest(UserMock.FINGERPRINT.value).apply { refreshToken = token }
//        val newToken = authService.refresh(refreshRequest).first
//        val principal = tokenManager.parseTokenPrincipal(newToken.accessToken)
//        assertEquals(principal.userId, user.id)
//    }
//
//    @Test
//    fun `refresh expired token`() {
//        userHelper.registerUser().also { verifyEmailService.deleteById(it.id) }
//        val token = authService.login(UserMock.loginRequest()).second
//        refreshTokenDao.findByFingerprintDeviceId(UserMock.FINGERPRINT.value)
//            .get().apply { expiresAt = Date.from(Instant.now()) }
//        val refreshRequest = RefreshRequest(UserMock.FINGERPRINT.value).apply { refreshToken = token }
//        assertThrows<CorruptedTokenException> { authService.refresh(refreshRequest) }
//    }
//
//    @Test
//    fun `refresh invalid token`() {
//        userHelper.registerUser().also { verifyEmailService.deleteById(it.id) }
//        val token = authService.login(UserMock.loginRequest()).second
//        refreshTokenDao.findByFingerprintDeviceId(UserMock.FINGERPRINT.value)
//            .get().apply { hash = "another hash" }
//        val refreshRequest = RefreshRequest(UserMock.FINGERPRINT.value).apply { refreshToken = token }
//        assertThrows<CorruptedTokenException> { authService.refresh(refreshRequest) }
//    }
//
////    @Test
////    fun `reset password`() {
////        val user = userHelper.registerUser().also { verifyEmailService.deleteById(it.id) }
////        val request = SetNewPasswordRequest(UserMock.PASSWORD.value, "new_password")
////        authService.setNewPassword(request)
////        val hash = userDao.findById(user.id).get().hash
////        assertTrue { passwordEncoder.matches("new_password", hash) }
////    }
////
////    @Test
////    fun `reset password without email verification`() {
////        val user = userHelper.registerUser()
////        val request = SendEmailToResetPasswordRequest(user.email)
////        assertThrows<EmailNotVerifiedException> { authService.sendEmailToResetPassword(request) }
////    }
////
////    @Test
////    fun `reset password by invalid token`() {
////        val user = userHelper.registerUser().also { verifyEmailService.deleteById(it.id) }
////        authService.sendEmailToResetPassword(SendEmailToResetPasswordRequest(user.email))
////        val request = ResetPasswordRequest("invalid", "new", UserMock.FINGERPRINT.value)
////        assertThrows<NotFoundRequestToChangePassword> { authService.resetPassword(request) }
////    }
////
////    @Test
////    fun `reset password by already used reset token`() {
////        val user = userHelper.registerUser().also { verifyEmailService.deleteById(it.id) }
////        authService.sendEmailToResetPassword(SendEmailToResetPasswordRequest(user.email))
////        val token = resetPasswordService.findByUserId(user.id).get()
////        val request = ResetPasswordRequest(token.token, "newpassword", UserMock.FINGERPRINT.value)
////        assertThrows<NotFoundRequestToChangePassword> {
////            authService.resetPassword(request)
////            authService.resetPassword(request)
////        }
////    }
//}
