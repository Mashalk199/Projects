import { Tutor } from "@/types/tutors";
import { useDrag, useDrop } from "react-dnd";
import {
  Card,
  CardBody,
  CardHeader,
  Heading,
  HStack,
  VStack,
  Text,
} from "@chakra-ui/react";
import { useRef } from "react";

const ITEM_TYPE = "TUTOR";

interface Props {
  tutor: Tutor;
  index: number;
  moveCard: (from: number, to: number) => void;
}

export default function TutorCard({ tutor, index, moveCard }: Props) {
  const ref = useRef<HTMLDivElement>(null);

  const [, drop] = useDrop({
    accept: ITEM_TYPE,
    hover(item: { index: number }) {
      if (!ref.current || item.index === index) return;
      moveCard(item.index, index);
      item.index = index;
    },
  });

  const [{ isDragging }, drag] = useDrag({
    type: ITEM_TYPE,
    item: { index },
    collect: (monitor) => ({
      isDragging: monitor.isDragging(),
    }),
  });

  drag(drop(ref)); // combine refs

  return (
    <div ref={ref} style={{ opacity: isDragging ? 0.5 : 1 }}>
      <Card
        width="50rem"
        borderWidth="2px"
        borderColor="gray.50"
        borderRadius="md"
        boxShadow="lg"
      >
        <HStack justify="space-between" pr="1rem" align="center" spacing={20}>
          {/* Wraps the text in a box so that button can be centered in the middle to its right 
                              with HStack */}
          <CardHeader>
            <Heading size="md">{tutor.username}</Heading>
          </CardHeader>
          <CardBody>
            <VStack align="stretch" spacing={2}>
              <Text>
                <strong>Availability:</strong>{" "}
                {tutor.availability === "full" ? "Full-Time" : "Part-Time"}
              </Text>
              <Text>
                <strong>Skills:</strong>{" "}
                {tutor.skillsList.length > 0
                  ? tutor.skillsList.join(", ")
                  : "N/A"}
              </Text>
              <Text>
                <strong>Credentials:</strong> {tutor.aCredentials.join(", ")}
              </Text>
              {tutor.prevRolesList.length > 0 && (
                <Text>
                  <strong>Previous Roles:</strong>{" "}
                  {tutor.prevRolesList.join(", ")}
                </Text>
              )}
            </VStack>
          </CardBody>
        </HStack>
      </Card>
    </div>
  );
}
