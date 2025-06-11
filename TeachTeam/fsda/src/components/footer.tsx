import { Box } from "@chakra-ui/react";

export default function Footer() {
  return (
    <Box
      as="footer"
      p={4}
      bgGradient="linear(to-l,rgb(140, 40, 202),rgb(255, 0, 115))"
      color="white"
      textAlign="center"
    >
      <h3>Website developed by Jay and Mashal</h3>
    </Box>
  );
}
