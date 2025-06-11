import { useApplications } from "@/context/ApplicationContext";
import { useAuth } from "@/context/AuthContext";
import { useCourses } from "@/context/CourseContext";
import { Lecturer } from "@/types/lecturers";
import { statusObj } from "@/types/statusObj";
import {
  Box,
  Button,
  Card,
  CardBody,
  CardHeader,
  Flex,
  Heading,
  HStack,
  Stack,
  StackDivider,
  VStack,
  Text,
  Checkbox,
  CheckboxGroup,
  Textarea,
} from "@chakra-ui/react";
import router from "next/router";
import { useEffect, useState } from "react";

export default function Hire() {
  const { applications, setApplications } = useApplications();
  const { courses } = useCourses();
  const { user, tutors, lecturers, setLecturers, setUser } = useAuth();
  if (!user) {
    return <div>Please log in to access this page.</div>;
  }
  const { courseId } = router.query; // Access the courseId from the query

  const parsedCourseId = Array.isArray(courseId) ? courseId[0] : courseId ?? "";
  const lecturerUser = user as Lecturer;

  // Compute lecturerRankings from the lecturerUser context
  const getLecturerRankings = () =>
    (lecturerUser.rankingList ?? []).find(
      (rankList) => rankList.course_id === parsedCourseId
    )?.candidateRanking ?? [];

  // Initialize state with the computed lecturerRankings
  const [selectedTutors, setSelectedTutors] = useState<number[]>(
    getLecturerRankings()
  );

  // Update local state whenever lecturerUser.rankingList or parsedCourseId changes,
  // or finally gets resolved after the INITIAL render:
  useEffect(() => {
    setSelectedTutors(getLecturerRankings());
  }, [lecturerUser.rankingList, parsedCourseId]);

  let tutorSet = new Set();
  for (let i = 0; i < applications.length; ++i) {
    if (applications[i].course_id === parsedCourseId) {
      tutorSet.add(applications[i].tutor_id);
    }
  }
  const appliedTutors = tutors.filter((tutor) => tutorSet.has(tutor.user_id));
  const hireHandler = (tutorId: string) => {
    const query = new URLSearchParams({
      tutorId,
      courseId: parsedCourseId,
    });

    router.push(`/profiles?${query.toString()}`);
  };
  const rankingHandler = () => {
    const query = new URLSearchParams({
      courseId: parsedCourseId,
    });

    router.push(`/Rankings?${query.toString()}`);
  };
  const checkHandler = (values: string[]) => {
    // cast to number
    const numericValues = values.map(Number);
    setSelectedTutors(numericValues);
    // Below, we preserve the order of the candidateRanking and only include values present in numericValues
    // in order to filter the tutors
    const filteredRanking = getLecturerRankings().filter((rank) =>
      numericValues.includes(rank)
    );
    // Instead of tutors being unchecked and removed, some may be added. We calculate the added tutors here.
    const newUnrankedTutors = numericValues.filter(
      (x) => !getLecturerRankings().includes(x)
    );
    // Adds the new unranked tutors to the end of the candidateRanking list
    const newRankedTutors = filteredRanking.concat(newUnrankedTutors);
    // Store lecturers course list of tutor rankings
    const updatedRankingList = (lecturerUser.rankingList ?? []).map(
      (rankList) =>
        rankList.course_id === parsedCourseId
          ? { course_id: parsedCourseId, candidateRanking: newRankedTutors }
          : rankList
    );
    // If lecturer hasn't ranked or selected any tutors for a course,
    // a new course object is added to rankingList
    if (
      !(lecturerUser.rankingList ?? []).find(
        (rankList) => rankList.course_id === parsedCourseId
      )
    ) {
      updatedRankingList.push({
        course_id: parsedCourseId,
        candidateRanking: newRankedTutors,
      });
    }

    // Update lecturer list
    const updatedLecturers = lecturers.map((lecturer) =>
      lecturer.user_id === lecturerUser.user_id
        ? {
            ...lecturer,
            rankingList: updatedRankingList,
          }
        : lecturer
    );
    // Update context and localStorage
    setLecturers(updatedLecturers);
    const updatedUser = { ...lecturerUser, rankingList: updatedRankingList };
    setUser(updatedUser);

    localStorage.setItem("lecturers", JSON.stringify(updatedLecturers));
    localStorage.setItem("currentUser", JSON.stringify(updatedUser));
  };

  return (
    <Flex flex="1" align="center" direction="column">
      <Heading mt="4">Hire future tutors for your class!</Heading>
      <Heading mt="4" size="md">
        {parsedCourseId} -{" "}
        {
          courses.find((course) => course.course_id === parsedCourseId)
            ?.course_name
        }
      </Heading>
      <Text mt={5}>Select a maximum of 5 tutors for hiring</Text>
      <CheckboxGroup value={selectedTutors.map(String)} onChange={checkHandler}>
        <Stack
          divider={<StackDivider />}
          spacing="2"
          pt="5"
          height="400px"
          overflowY="auto"
          overflowX="hidden"
          width="52rem"
        >
          {appliedTutors.map((tutor) => (
            <Box>
              <Card width="50rem">
                <HStack justify="space-between" pr="1rem">
                  {/* Wraps the text in a box so that button can be centered in the middle to its right 
                              with HStack */}
                  <Box>
                    <CardHeader>
                      <Heading size="md">{tutor.username}</Heading>
                    </CardHeader>
                    <CardBody>
                      <VStack align="stretch" spacing={2}>
                        <Text>
                          <strong>Availability:</strong>{" "}
                          {tutor.availability === "full"
                            ? "Full-Time"
                            : "Part-Time"}
                        </Text>
                        <Text>
                          <strong>Skills:</strong>{" "}
                          {tutor.skillsList.length > 0
                            ? tutor.skillsList.join(", ")
                            : "N/A"}
                        </Text>
                        <Text>
                          <strong>Credentials:</strong>{" "}
                          {tutor.aCredentials.join(", ")}
                        </Text>
                        {tutor.prevRolesList.length > 0 && (
                          <Text>
                            <strong>Previous Roles:</strong>{" "}
                            {tutor.prevRolesList.join(", ")}
                          </Text>
                        )}
                      </VStack>
                    </CardBody>
                  </Box>
                  <Flex direction="column" gap={12} height="100%" p={10}>
                    <Button
                      onClick={() => hireHandler(tutor.user_id)}
                      colorScheme="blue"
                    >
                      View detailed application
                    </Button>
                    <Checkbox
                      // Disables checkboxes when 5 tutors have been selected
                      isDisabled={
                        selectedTutors.length >= 5 &&
                        !selectedTutors.includes(Number(tutor.user_id))
                      }
                      // Automatically selects if tutor has been previously selected by lecturer
                      isChecked={selectedTutors.includes(Number(tutor.user_id))}
                      size="lg"
                      colorScheme="orange"
                      value={tutor.user_id.toString()}
                    >
                      Select for hiring
                    </Checkbox>
                  </Flex>
                </HStack>
              </Card>
            </Box>
          ))}
        </Stack>
      </CheckboxGroup>
      <Button mt={6} colorScheme="blue" onClick={rankingHandler}>
        Rank selected candidates
      </Button>
    </Flex>
  );
}
