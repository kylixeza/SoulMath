package com.ramiyon.soulmath.util

import com.ramiyon.soulmath.data.source.local.database.enitity.DailyXpEntity
import com.ramiyon.soulmath.data.source.local.database.enitity.LeaderboardEntity
import com.ramiyon.soulmath.data.source.local.database.enitity.StudentEntity
import com.ramiyon.soulmath.data.source.remote.api.response.answer.AnswerResponse
import com.ramiyon.soulmath.data.source.remote.api.response.leaderboard.LeaderboardResponse
import com.ramiyon.soulmath.data.source.remote.api.response.learning_journey.GameDifficultyLearningJourneyResponse
import com.ramiyon.soulmath.data.source.remote.api.response.learning_journey.GameLearningJourneyResponse
import com.ramiyon.soulmath.data.source.remote.api.response.learning_journey.LearningJourneyResponse
import com.ramiyon.soulmath.data.source.remote.api.response.learning_journey.MaterialLearningJourneyResponse
import com.ramiyon.soulmath.data.source.remote.api.response.material.MaterialDetailResponse
import com.ramiyon.soulmath.data.source.remote.api.response.material.MaterialResponse
import com.ramiyon.soulmath.data.source.remote.api.response.question.QuestionResponse
import com.ramiyon.soulmath.data.source.remote.api.response.student.StudentBody
import com.ramiyon.soulmath.data.source.remote.api.response.student.StudentResponse
import com.ramiyon.soulmath.data.source.remote.firebase.reseponse.MaterialLearningPurposeResponse
import com.ramiyon.soulmath.data.source.remote.firebase.reseponse.MaterialOnBoardResponse
import com.ramiyon.soulmath.domain.model.*
import com.ramiyon.soulmath.domain.model.learning_journey.GameDifficultyLearningJourney
import com.ramiyon.soulmath.domain.model.learning_journey.GameLearningJourney
import com.ramiyon.soulmath.domain.model.learning_journey.LearningJourney
import com.ramiyon.soulmath.domain.model.learning_journey.MaterialLearningJourney
import com.ramiyon.soulmath.domain.model.material.Material
import com.ramiyon.soulmath.domain.model.material.MaterialDetail
import com.ramiyon.soulmath.domain.model.material.MaterialLearningPurpose
import com.ramiyon.soulmath.domain.model.material.MaterialOnBoard

fun Student.toStudentBody() = StudentBody(
    studentId, address, avatar, username, email, phoneNumber, xp
)

fun Student.toStudentEntity() = StudentEntity(
    studentId, address, avatar, username, email, phoneNumber, xp
)

fun StudentResponse.toStudentEntity() = StudentEntity(
    studentId, address, avatar, username, email, phoneNumber, xp
)

fun StudentResponse.toStudent() = Student(
    studentId, address, avatar, username, email, phoneNumber, xp
)

fun StudentEntity.toStudentBody() = StudentBody(
    studentId, address, avatar, username, email, phoneNumber, xp
)

fun StudentEntity.toStudent() = Student(
    studentId, address, avatar, username, email, phoneNumber, xp
)

fun LeaderboardResponse.toLeaderboard() = Leaderboard(
    studentId, username, avatar, xp, rank
)

fun LeaderboardResponse.toLeaderboardEntity() = LeaderboardEntity(
    studentId, username, avatar, xp, rank
)

fun LeaderboardEntity.toLeaderboard() = Leaderboard(
    studentId, username, avatar, xp, rank
)

fun DailyXpEntity.toDailyXp() = DailyXp(
    dailyXpId, dailyXp, day, dayTaken, isTaken
)

fun LearningJourneyResponse.toLearningJourney() = LearningJourney(
    moduleId, moduleTitle, moduleImage, moduleIconLocked, moduleIconUnlocked, isModuleUnlocked,
    materialLearningJourneyResponse.toMaterialLearningJourney(), gameLearningJourneyResponses.map { it.toGameLearningJourney() }
)

private fun MaterialLearningJourneyResponse.toMaterialLearningJourney() = MaterialLearningJourney(
    moduleId, materialPercentage, currentMaterialId
)

fun MaterialOnBoardResponse.toMaterialOnBoard() = MaterialOnBoard(
    materialId, page, gif, upperImage, lowerImage, description
)

fun MaterialLearningPurposeResponse.toMaterialLearningPurpose() = MaterialLearningPurpose(
    materialId, chapter, purposes
)

fun MaterialResponse.toMaterial() = Material(
    subModuleId, materialId, materialImage, subModuleTitle, videoUrl, xpEarned, isUnlocked
)

fun MaterialDetailResponse.toMaterialDetail() = MaterialDetail(
    videoUrl, xpEarned, isFavorite
)

fun QuestionResponse.toQuestion() = Question(
    gameId, questionId, question, questionImage, answers.map { it.toAnswer() }
)

fun AnswerResponse.toAnswer() = Answer(
    questionId, answer, isRightAnswer
)

private fun GameLearningJourneyResponse.toGameLearningJourney() = GameLearningJourney(
    moduleId, subModuleId, subModuleTitle, gameIds.toGameDifficultyLearningJourney(), isGameUnlocked, gamePercentage,
    isDifficultiesUnlocked.toGameDifficultyLearningJourney(), isDifficultiesCrowned.toGameDifficultyLearningJourney()
)

private fun<T> GameDifficultyLearningJourneyResponse<T>.toGameDifficultyLearningJourney() = GameDifficultyLearningJourney(
    easy, medium, hard
)