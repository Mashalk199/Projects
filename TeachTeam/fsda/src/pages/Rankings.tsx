import { useAuth } from "@/context/AuthContext";
import { Lecturer } from "@/types/lecturers";
import {
  Box,
  Button,
  Card,
  CardBody,
  CardHeader,
  CheckboxGroup,
  Flex,
  Heading,
  HStack,
  Stack,
  StackDivider,
  Text,
  VStack,
} from "@chakra-ui/react";
import { useRouter } from "next/router";
import TutorCard from "@/components/TutorCard";
import { useState } from "react";
import { Tutor } from "@/types/tutors";

export default function rankings() {
  const { user, tutors, lecturers, setUser, setLecturers } = useAuth();
  const router = useRouter();
  if (!user) {
    return <div>Please log in to access this page.</div>;
  }

  const lecturerUser = user as Lecturer;

  const { courseId } = router.query; // Access the courseId from the query
  const parsedCourseId = Array.isArray(courseId) ? courseId[0] : courseId ?? "";

  const rankingList = lecturerUser.rankingList.find(
    (rankList) => rankList.course_id === parsedCourseId
  )?.candidateRanking;

  // Gets all the tutor objects from the context, but in the order read from rankingList to preserve ranking
  let tutorList = (rankingList ?? [])
    .map((tutor_id) =>
      tutors.find((tutor) => Number(tutor.user_id) === tutor_id)
    )
    .filter((tutor): tutor is Tutor => tutor !== undefined);

  const [rankedTutors, setRankedTutors] = useState<Tutor[]>(tutorList);

  const moveCard = (from: number, to: number) => {
    const updated = [...rankedTutors];
    const [moved] = updated.splice(from, 1);
    updated.splice(to, 0, moved);
    setRankedTutors(updated);

    // Save immediately after drag completes
    const newRanking = updated.map((t) => Number(t.user_id));
    const lecturerUser = user as Lecturer;

    const updatedRankingList = (lecturerUser.rankingList ?? []).map((r) =>
      r.course_id === parsedCourseId
        ? { ...r, candidateRanking: newRanking }
        : r
    );

    // If not yet in the list, add new entry
    if (
      !(lecturerUser.rankingList ?? []).some(
        (r) => r.course_id === parsedCourseId
      )
    ) {
      updatedRankingList.push({
        course_id: parsedCourseId,
        candidateRanking: newRanking,
      });
    }

    const updatedLecturer = {
      ...lecturerUser,
      rankingList: updatedRankingList,
    };

    // Save to context and localStorage
    setUser(updatedLecturer);
    setLecturers((prev) =>
      prev.map((l) =>
        l.user_id === updatedLecturer.user_id ? updatedLecturer : l
      )
    );
    localStorage.setItem("lecturers", JSON.stringify(lecturers));
    localStorage.setItem("currentUser", JSON.stringify(updatedLecturer));
  };

  return (
    <Flex flex="1" align="center" direction="column">
      <Heading mt="4">Rank your tutors by dragging and dropping</Heading>

      <Text mt={5}>Ranks are automatically saved</Text>
      <Stack
        divider={<StackDivider />}
        spacing="2"
        pt="5"
        height="500px"
        overflowY="auto"
        overflowX="hidden"
        width="52rem"
      >
        {rankedTutors.map((tutor, index) => (
          <TutorCard
            key={tutor.user_id}
            tutor={tutor}
            index={index}
            moveCard={moveCard}
          />
        ))}
      </Stack>
    </Flex>
  );
}
